package org.apache.bark.accuracy

import org.apache.bark.common.PartitionPair

/**
  * Accurarcy configuration entity
  */
class AccuracyConfEntity {
  var source: String = _
  var target: String = _
  var dt: String = _
  var hour: String = _

  var accuracyMapping: List[AccuracyMapping] = List()
  var srcPartitions: List[PartitionPair] = List()
  var tgtPartitions: List[List[PartitionPair]] = List()
}