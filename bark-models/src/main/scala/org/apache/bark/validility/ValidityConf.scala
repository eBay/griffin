package org.apache.bark.validility

import com.ebay.bark.PartitionConfig

class ValidityConf {
  var dataSet: String = null

  var validityReq: List[ValidityReq] = List()

  var timePartitions: List[PartitionConfig] = List()

  override def toString = {
    " dataSet: " + dataSet +
    " validityReq: " + validityReq +
    " timePartitions: " + timePartitions
  }
}