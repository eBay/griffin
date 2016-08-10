package org.apache.bark.accuracy

/**
  * mapping between source column and target column
  *
  */
class AccuracyMapping {
  var sourceColId: Int = 0
  var sourceColName: String = null
  var sourceConvertingFunctions: List[String] = List()

  var targetColId: Int = 0
  var targetColName: String = null
  var targetConvertingFunctions: List[String] = List()

  /**
    * matchFunction is still under specification, will implement it after requirement finalized.
    */
  var matchFunction: String = null
  var isPK: Boolean = false
}