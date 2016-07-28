/**
  * Accuracy of source data comparing with target data
  *
  * Purpose: suppose that each row of source data could be found in target data,
  * but there exists some errors resulting the data missing, in this progress
  * we count the missing data of source data set, which is not found in the target data set
  *
  *
  *
  */

package com.ebay.bark

import scala.annotation.migration
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD.rddToPairRDDFunctions
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

import org.apache.hadoop.conf._
import org.apache.hadoop.fs._
import com.fasterxml.jackson.module.scala._
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.apache.spark.rdd.RDD

import scala.collection.immutable.HashSet
import scala.collection.mutable.{ MutableList, HashSet => MutableHashSet, Map => MutableMap }

object Accu3 {

  def main(args: Array[String]) {

    val input = HdfsUtils.openFile(args(0))

    val outputPath = args(1) + System.getProperty("file.separator")

    //add files for job scheduling
    val startFile = outputPath + "_START"
    val resultFile = outputPath + "_RESULT"
    val doneFile = outputPath + "_FINISHED"
    val missingFile = outputPath + "missingRec.txt"

    val mapper = new ObjectMapper()
    mapper.registerModule(DefaultScalaModule)

    //read the config info of comparison
    val configure = mapper.readValue(input, classOf[AccuracyConf])

    val conf = new SparkConf().setAppName("Acc")
    val sc = new SparkContext(conf)
    val sqlContext = new org.apache.spark.sql.hive.HiveContext(sc)

    //add spark applicationId for debugging
    val applicationId = sc.applicationId
    HdfsUtils.writeFile(startFile, applicationId)

    //get source data
    val sql1 = "SELECT * FROM  " + configure.source + PartitionUtils.generateWhereClause(configure.srcPartitions)
    println(sql1)
    val sojdf = sqlContext.sql(sql1).rdd
    println("sojdf:" + sojdf.count())

    //get target data
    val sql2 = PartitionUtils.generateTargetSQLClause(configure.target, configure.tgtPartitions)
    println(sql2)
    val bedf = sqlContext.sql(sql2).rdd
    println("bedf:" + bedf.count())

    val mp = configure.accuracyMapping

    //the key column info, to match different rows between source and target
    val sojKeyIndexList = MutableList[scala.Tuple2[Int, String]]()
    val beKeyIndexList = MutableList[scala.Tuple2[Int, String]]()

    //the value column info, to be compared with between the match rows
    val sojValueIndexList = MutableList[scala.Tuple2[Int, String]]()
    val beValueIndexList = MutableList[scala.Tuple2[Int, String]]()

    //get the key and value column info from config
    for (i <- mp) {
      if (i.isPK) {

        val sojkey = scala.Tuple2(i.sourceColId, i.sourceColName)
        sojKeyIndexList += sojkey

        val bekey = scala.Tuple2(i.targetColId, i.targetColName)
        beKeyIndexList += bekey

      }

      val sojValue = scala.Tuple2(i.sourceColId, i.sourceColName)
      sojValueIndexList += sojValue

      val beValue = scala.Tuple2(i.targetColId, i.sourceColName)
      beValueIndexList += beValue

    }

    def toTuple[A <: Object](as: MutableList[A]): Product = {
      val tupleClass = Class.forName("scala.Tuple" + as.size)
      tupleClass.getConstructors.apply(0).newInstance(as: _*).asInstanceOf[Product]
    }

    //--1. convert data into same format (key, value)--

    //convert source data rows into (key, ("__source__", valMap)), key is the key column value tuple, valMap is the value map of row
    val sojkvs = sojdf.map { row =>
      val kk = sojKeyIndexList map { t => row.getString(t._1) }

      val kkk = toTuple(kk)

      val v = sojValueIndexList.foldLeft(MutableMap[String, Any]()) { (c, x) => c(x._2) = row.get(x._1); c }

      (kkk -> ("__source__", v))
    }

    //convert source data rows into (key, ("__target__", valMap)), key is the key column value tuple, valMap is the value map of row
    val bekvs = bedf.map { row =>
      val kk = beKeyIndexList map { t => row.getString(t._1) }

      val kkk = toTuple(kk)

      val v = beValueIndexList.foldLeft(MutableMap[String, Any]()) { (c, x) => c(x._2) = row.get(x._1); c }

      (kkk -> ("__target__", v))
    }

    //union the same format data from source and target
    val allkvs = sojkvs.union(bekvs)

    println("all events before reduce")

    //--2. create container of data--

    //sequence operator of container creation
    def seqContainer(container: (List[MutableMap[String, Any]], HashSet[MutableMap[String, Any]]), tp: (String, MutableMap[String, Any])) = {
      if (tp._1.equals("__source__")) (tp._2 :: container._1, container._2) else (container._1, container._2 + tp._2)
    }

    //combine operator of container creation
    def combContainer(con1: (List[MutableMap[String, Any]], HashSet[MutableMap[String, Any]]), con2: (List[MutableMap[String, Any]], HashSet[MutableMap[String, Any]])) = {
      (con1._1 ++ con2._1, con1._2 ++ con2._2)
    }

    //create container for each key, the tuple of a list and a set, the list contains source data, while the set contains target ones
    val kvsContainer = allkvs.aggregateByKey((List[MutableMap[String, Any]](), HashSet[MutableMap[String, Any]]()))(seqContainer, combContainer)

    //--3. get missed count of source data--

    //with the same key, for each source data in list, if it does not exists in the target set, one missed data found
    def seqMissed(cnt: (Int, List[String]), kv: (Product, (List[MutableMap[String, Any]], HashSet[MutableMap[String, Any]]))) = {
      val ls = kv._2._1
      val st = kv._2._2

      val ss = ls.foldLeft((0, List[String]())) { (c, mp) =>
        if (st.contains(mp)) {
          println("c: " + c)
          c
        } else {
          if (st.size == 0) {
            (c._1 + 1, mp.toString :: c._2)
          } else {
            (c._1 + 1, mp.toString :: c._2)
          }
        }
      }

      println("ss: " + ss._1 + ", _2: " + ss._2.length)

      (cnt._1 + ss._1, ss._2 ::: cnt._2)
    }

    //add missed count of each partition
    def combMissed(cnt1: (Int, List[String]), cnt2: (Int, List[String])) = {
      //      println("=== add: " + cnt1 + " + " + cnt2);

      println("cnt1: " + cnt1._2.length + ", cnt2: " + cnt2._2.length)
      (cnt1._1 + cnt2._1, cnt1._2 ::: cnt2._2)
    }

    //count missed source data

    val missed = kvsContainer.aggregate(0, List[String]())(seqMissed, combMissed)

    //output: need to change
    println("missed count : " + missed._1)

    HdfsUtils.writeFile(resultFile, ((1 - missed._1.toDouble / sojdf.count()) * 100).toString())

    val sb = new StringBuilder
    missed._2.foreach { item =>
      sb.append(item)
      sb.append("\n")

    }

    HdfsUtils.writeFile(missingFile, sb.toString())
    HdfsUtils.createFile(doneFile)

    sc.stop()

  }

}