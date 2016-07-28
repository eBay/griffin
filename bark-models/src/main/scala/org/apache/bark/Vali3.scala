package com.ebay.bark

import com.fasterxml.jackson.core.{JsonFactory, JsonGenerator}
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.mllib.linalg.{Vector, Vectors}
import org.apache.spark.mllib.stat.{MultivariateStatisticalSummary, Statistics}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Row}
import org.apache.spark.sql.types.DataType

import scala.collection.immutable.HashSet
import scala.collection.mutable.{Map, MutableList}
import scala.collection.{Map, mutable}
import scala.util.matching.Regex

object Vali3 {

  def main(args: Array[String]): Unit ={

    val input = HdfsUtils.openFile(args(0))

    val outputPath = args(1) + System.getProperty("file.separator")

    //add files for job scheduling
    val startFile = outputPath + "_START"
    val resultFile = outputPath + "_RESULT"
    val doneFile = outputPath + "_FINISHED"

    val mapper = new ObjectMapper()
    mapper.registerModule(DefaultScalaModule)

    //read the config info of comparison
    val configure = mapper.readValue(input, classOf[ValidityConf])

    val conf = new SparkConf().setAppName("Vali")
    val sc: SparkContext = new SparkContext(conf)
    val sqlContext = new org.apache.spark.sql.hive.HiveContext(sc)

    //add spark applicationId for debugging
    val applicationId = sc.applicationId
    HdfsUtils.writeFile(startFile, applicationId)

    //get data
    val sojdf = sqlContext.sql("SELECT * FROM " + configure.dataSet + PartitionUtils.generateWhereClause(configure.timePartitions))
    val dfCount = sojdf.count()
    println("sojdf: " + dfCount)


    //-- algorithm --
    calcVali(sc, configure, sojdf)

    //--output metrics data--
    val out = HdfsUtils.createFile(resultFile)
    mapper.writeValue(out, configure)

    HdfsUtils.createFile(doneFile)

    sc.stop()
  }

  def calcVali(sc: SparkContext, configure: ValidityConf, sojdf: DataFrame) : Unit = {
    val dfCount = sojdf.count()
    println("sojdf:" + dfCount)

    //--1. get all cols name, and types--
    val fnts = sojdf.schema.fields.map(x => (x.name, x.dataType.simpleString)).toMap

    val isNumRegex = "^[Ii]nt|[Ss]hort|[Ll]ong|[Bb]yte|[Ff]loat|[Dd]ouble$"
    def isNum(tp: String): Boolean = tp.matches(isNumRegex)

    //get col type
    val req: List[ValidityReq] = configure.validityReq.map { r =>
      val fv = fnts.getOrElse(r.colName, None)
      if (fv != None) {
        r.colType = fv.toString
        r.isNum = isNum(r.colType)
      }
      r
    }

    //--2. calc num cols metrics--
    val numcols = req.filter(r => r.isNum)

    val numIdx = numcols.map(c => c.colId).toArray

    //median number function
    def funcMedian(df: DataFrame, col: Int): Double = {
      val mp = df.rdd.map(v => (v.getString(col).toDouble, 1L)).reduceByKey(_+_)
      val allCnt = dfCount
      val cnt = mp.sortByKey().collect()
      var tmp, tmp1 = 0L
      var median, median1 = cnt(0)._1
      if (allCnt % 2 != 0) {
        for (i <- 0 until cnt.length if (tmp < allCnt / 2 + 1)) {
          tmp += cnt(i)._2
          median = cnt(i)._1
        }
        median
      } else {
        for (i <- 0 until cnt.length if (tmp1 < allCnt / 2 + 1)) {
          tmp1 += cnt(i)._2
          median1 = cnt(i)._1
          if (tmp < allCnt / 2) {
            tmp = tmp1
            median = median1
          }
        }
        (median + median1) / 2
      }
    }

    //match num metrics request
    def getNumStats(smry: MultivariateStatisticalSummary, df: DataFrame, op: Int, col: Int): Any = {
      val i = numIdx.indexWhere(_ == col)
      if (i >= 0) {
        op match {
          case MetricsConf.totalCount => smry.count
          case MetricsConf.maximum => smry.max(i)
          case MetricsConf.minimum => smry.min(i)
          case MetricsConf.mean => smry.mean(i)
          case MetricsConf.median => funcMedian(df, col)
//          case MetricsConf.variance => smry.variance(i)
//          case MetricsConf.numNonZeros => smry.numNonzeros(i)
          case _ => None
        }
      }
    }

    if (numcols.length > 0) {
      //calc metrics of all numeric cols once
      val numcolVals = sojdf.rdd.map { row =>
        val vals = numIdx.map(i => row.getString(i).toDouble)
        Vectors.dense(vals)
      }
      val summary = Statistics.colStats(numcolVals)

      //get numeric metrics from summary
      numcols.foreach(vr => vr.metrics.foreach(mc => mc.result = getNumStats(summary, sojdf, mc.name, vr.colId)))
    }

    //--3. calc str/other cols metrics--
    val strcols = req.filter(r => !r.isNum)

    //count function
    def funcCount(df: DataFrame, col: Int): Long = {
      dfCount
    }
    //null count function
    def funcNullCount(df: DataFrame, col: Int): Long = {
      val nullRow = df.rdd.map(row => if (row.isNullAt(col)) 1L else 0)
      nullRow.fold(0)((a,b)=>a+b)
    }
    //unique count function
    def funcUniqueCount(df: DataFrame, col: Int): Long = {
      val mp = df.rdd.map(v=>(v.getString(col)->1L))
      val rs = mp.reduceByKey(_+_)
      rs.count()
    }
    //duplicate count function
    def funcDuplicateCount(df: DataFrame, col: Int): Long = {
      val mp = df.rdd.map(v=>(v.getString(col)->1L))
      val rs = mp.reduceByKey(_+_)
      rs.aggregate(0)((s, v) => if (v._2 == 1) s else s + 1, (s1, s2) => s1 + s2)
    }

    //regex and match str metrics request
    def getStrResult(df: DataFrame, op: Int, col: Int): Any = {
      op match {
        case MetricsConf.totalCount => funcCount(df, col)
        case MetricsConf.nullCount => funcNullCount(df, col)
        case MetricsConf.uniqueCount => funcUniqueCount(df, col)
        case MetricsConf.duplicateCount => funcDuplicateCount(df, col)
        case _ => None
      }
    }

    if (strcols.length > 0) {
      //calc str metrics one by one
      strcols.foreach(vr => vr.metrics.foreach(mc => mc.result = getStrResult(sojdf, mc.name, vr.colId)))
    }

    //union the num cols and str cols metrics, and put the result into configure object
    val rsltCols = numcols.union(strcols)
    configure.validityReq = rsltCols

    println("== result ==\n" + rsltCols)

  }

}