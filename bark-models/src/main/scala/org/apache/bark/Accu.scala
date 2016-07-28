package com.ebay.bark

import scala.annotation.migration
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD.rddToPairRDDFunctions
import org.apache.spark.sql.{DataFrame, Dataset}

import scala.collection.mutable


object Accu {

  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("Acc")
    val sc = new SparkContext(conf)
    val sqlContext = new org.apache.spark.sql.hive.HiveContext(sc)
    val sojdf = sqlContext.sql("SELECT * FROM soj_view_event where dt ='20160610' and hour = 22").rdd
    //TODO, call def dropDuplicates(colNames: Array[String]) to filter out some duplicate records in case soj data quality issue 
    val bedf = sqlContext.sql("SELECT * FROM be_view_event_queue where dt ='20160610' and hour = 22").rdd
    val sojKeyIndexList = List((0, "uid"), (14, "eventtimestamp"))
    val beKeyIndexList = List((0, "uid"), (16, "eventtimestamp"))
    val sojValueIndexList = List((9, "leaf"),(0, "uid"), (14, "eventtimestamp"))
    val beValueIndexList = List((11, "leaf"),(0, "uid"), (16, "eventtimestamp"))
    
    def toTuple[A <: Object](as:List[A]):Product = {
      val tupleClass = Class.forName("scala.Tuple" + as.size)
      tupleClass.getConstructors.apply(0).newInstance(as:_*).asInstanceOf[Product]
    }

    val sojkvs = sojdf.map { row =>
      //composite key(uid,timestamp)
      val kk = sojKeyIndexList map { t => row.getString(t._1)}
      
      val kkk = toTuple(kk)
      
//      val k = (row.get(sojKeyIndexList.head._1), row.get(sojKeyIndexList.last._1))

      val v = sojValueIndexList.foldLeft(scala.collection.mutable.Map[String, Any]("__group__" -> "__source__")) { (c, x) => c(x._2) = row.get(x._1); c }

      (kkk, v)
    }
    
    //we can reduce soj first to check soj's own DQ
    
    val sojkvsAfterreduce = sojkvs.reduceByKey((s1,s2)=>
      s1 ++ s2
      )
      

    val bekvs = bedf.map { row =>
      val kk = beKeyIndexList map { t => row.getString(t._1)}
      
      val kkk = toTuple(kk)

//      val k = (row.get(beKeyIndexList.head._1), row.get(beKeyIndexList.last._1))

      val v = beValueIndexList.foldLeft(scala.collection.mutable.Map[String, Any]("__group__" -> "__target__")) { (c, x) => c(x._2) = row.get(x._1); c }

      (kkk, v)
    }

    val allevents = sojkvs.union(bekvs)

    val result = allevents.reduceByKey((soje, bee) =>

      if (soje.get("__group__") == Some("__source__")) {
        if (bee.get("__group__") == Some("__target__")) {
          //comparing values besides group label
          if ((bee - "__group__") == (soje - "__group__")) soje + ("__result__" -> "YES")
          else soje + ("__result__" -> "NO")
        } else {
          soje + ("__result__" -> "Duplicates")
        }

      } 
      else {
        bee /*+ ("__result__" -> "be")*/
      }
      )
      
      val missed = result.filter(x => x._2.get("__result__")== Some("NO") && x._2.get("__group__")== Some("__source__")).count()
      
      println("missed count : "+missed)
  }

}