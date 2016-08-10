package org.apache.bark.validility

import org.apache.bark.common.PartitionPair

class ValidityConfEntity {
  var dataSet: String = null

  var validityReq: List[ValidityReq] = List()

  var timePartitions: List[PartitionPair] = List()
}