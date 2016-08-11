package org.apache.bark.accuracy

import org.apache.bark.common.PartitionPair

/**
  * Accurarcy configuration entity
  */
class AccuracyConfEntity {
  var source: String = null
  var target: String = null
  var dt: String = null
  var hour: String = null

  var accuracyMapping: List[AccuracyMapping] = List()
  var srcPartitions: List[PartitionPair] = List()
  var tgtPartitions: List[List[PartitionPair]] = List()
}