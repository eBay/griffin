package org.apache.bark.validility

import org.apache.bark.common.PartitionPair

class ValidityConfEntity {
  var dataSet: String = _

  var validityReq: List[ValidityReq] = List()

  var timePartitions: List[PartitionPair] = List()

  override def toString = "dataSet: " +dataSet+", validityReq: " +validityReq

//  {
//    s"dataSet: $dataSet, validityReq: $validityReq"
//  }
}