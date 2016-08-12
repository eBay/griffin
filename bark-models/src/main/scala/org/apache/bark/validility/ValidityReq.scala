package org.apache.bark.validility

class ValidityReq {
  var colId: Int = _
  var colName: String = _

  var colType: String = _
  var isNum: Boolean = _

  var metrics: List[MetricsReq] = List()

  override def toString = {
    s"colId: $colId, colName: $colName, colType: $colType, isNum: $isNum, metrics: $metrics"
  }
}

class MetricsReq {
  var name: Int = _
  var result: Any = _

  override def toString = {
    s"name: $name, result: $result"
  }
}