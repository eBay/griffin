package org.apache.bark.validility

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.stat.Statistics

object Validility extends App {
    val conf = new SparkConf().setAppName("Validility")
    val sc = new SparkContext(conf)

    val sqlContext = new org.apache.spark.sql.hive.HiveContext(sc)
    val sojdf = sqlContext.sql("SELECT * FROM soj_view_event where dt ='20160619' and hour = 20")
    //table summary information for numeric columns
    sojdf.describe().show()
    //generate table 
    println(sojdf.count())
    
    //get all cols name, and types
    val fnts = sojdf.schema.fields.map(x=>(x.name,x.dataType))
    
    val strcols = fnts.filter(x => x._2.simpleString == "string").map(x =>x._1)
    
    //for num cols
    //since we dont't know the excatly types of columns, we just calculate uids
    val uids = sojdf.rdd.map(row => Vectors.dense(row.getAs[String]("uid").toDouble))
    val summary = Statistics.colStats(uids)

    // Compute column summary statistics.
    println(summary.max)  
    println(summary.mean) 
    println(summary.min)  
    
    println(summary.variance)  
    println(summary.numNonzeros)
    
    //for string cols
    val titlecol = sojdf.rdd.filter(x => x.getAs[String]("itmtitle") != null)
    
    println(titlecol.count())

    //test comment by lll
    sc.stop()
  
}