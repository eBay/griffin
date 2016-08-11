package org.apache.bark.validility

class ValidityReq {
  var colId: Int = 0
  var colName: String = _

  var colType: String = _
  var isNum: Boolean = _

  var metrics: List[MetricsReq] = Nil

  override def toString = {
    s"colId: $colId, colName: $colName, colType: $colType, isNum: $isNum, metrics: $metrics"
  }
}

class MetricsReq {
  var name: Int = 0
  var result: Any = _

  override def toString = {
    s"name: $name, result: $result"
  }
}