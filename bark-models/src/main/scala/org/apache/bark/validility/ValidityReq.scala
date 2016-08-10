package org.apache.bark.validility

class ValidityReq {
  var colId: Int = 0
  var colName: String = null

  var colType: String = null
  var isNum: Boolean = false

  var metrics: List[MetricsReq] = List()

  override def toString = {
    s"colId: $colId, colName: $colName, colType: $colType, isNum: $isNum, metrics: $metrics"
  }
}

class MetricsReq {
  var name: Int = 0
  var result: Any = null

  override def toString = {
    s"name: $name, result: $result"
  }
}