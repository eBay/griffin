package com.ebay.bark

class AccuracyConf {
  var source: String = null
  var target: String = null

  var accuracyMapping: List[AccuracyMapping] = List()

  var dt: String = null
  var hour: String = null

  var srcPartitions: List[PartitionConfig] = List()
  var tgtPartitions: List[List[PartitionConfig]] = List()
}