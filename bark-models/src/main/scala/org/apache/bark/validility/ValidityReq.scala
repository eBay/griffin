package org.apache.bark.validility

class ValidityReq {
  var colId: Int = 0
  var colName: String = null

  var colType: String = null
  var isNum: Boolean = false

  var metrics: List[MetricsReq] = List()
}
