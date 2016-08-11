package org.apache.bark.validility

import org.apache.bark.common.PartitionPair

class ValidityConfEntity {
  var dataSet: String = _

  var validityReq: List[ValidityReq] = Nil

  var timePartitions: List[PartitionPair] = Nil

  override def toString = {
    s"dataSet: $dataSet, validityReq: $validityReq"
  }
}