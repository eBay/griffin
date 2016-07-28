package com.ebay.bark

class MetricsReq {
  var name: Int = 0
  var result: Any = null

  override def toString = {
    " name: " + name +
    " result: " + result
  }
}
