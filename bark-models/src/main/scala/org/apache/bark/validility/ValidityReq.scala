package org.apache.bark.validility

class ValidityReq {
  var colId: Int = 0
  var colName: String = null

  var colType: String = null
  var isNum: Boolean = false

  var metrics: List[MetricsReq] = List()

  override def toString = {
    " colId: " + colId +
    " colName: " + colName +
    " colType: " + colType +
    " isNum: " + isNum +
    " metrics: " + metrics
  }
}
