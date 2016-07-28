package com.ebay.bark

class AccuracyMapping {
  val sourceColId: Int = 0
  var sourceColName: String = null
  var sourceConvertingFunctions: List[String] = List()

  val targetColId: Int = 0
  var targetColName: String = null
  var targetConvertingFunctions: List[String] = List()

  var matchFunction: String = null
  var isPK: Boolean = false
}