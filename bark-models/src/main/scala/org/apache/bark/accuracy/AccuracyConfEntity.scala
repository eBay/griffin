package org.apache.bark.accuracy

import com.ebay.bark.PartitionConfig

/**
  * Accurarcy configuration entity
  */
class AccuracyConfEntity {
  var source: String = null
  var target: String = null
  var dt: String = null
  var hour: String = null

  var accuracyMapping: List[AccuracyMapping] = List()
  var srcPartitions: List[PartitionConfig] = List()
  var tgtPartitions: List[List[PartitionConfig]] = List()
}