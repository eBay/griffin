package com.ebay.bark.dataLoaderUtils

object FileLoaderUtil {
  def convertPath(path: String) : String = {
    path.replace("/", System.getProperty("file.separator"))
  }
}
