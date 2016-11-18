package org.apache.griffin.dataLoaderUtils

import org.apache.griffin.validility.ValidityConfEntity
import org.apache.griffin.accuracy.AccuracyConfEntity
import org.apache.spark.sql.{DataFrame, SQLContext}

abstract class DataLoader(val sqlContext: SQLContext) {
  def getAccuDataFrame(accu: AccuracyConfEntity) : (DataFrame, DataFrame)
  def getValiDataFrame(vali: ValidityConfEntity) : DataFrame
}
