package org.apache.bark.validility

import org.apache.bark.common.PartitionPair

class ValidityConf {
  var dataSet: String = null

  var validityReq: List[ValidityReq] = List()

  var timePartitions: List[PartitionPair] = List()

  override def toString = {
    " dataSet: " + dataSet +
    " validityReq: " + validityReq +
    " timePartitions: " + timePartitions
  }
}