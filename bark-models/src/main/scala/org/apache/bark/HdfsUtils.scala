package com.ebay.bark

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import java.io.File
import org.apache.hadoop.fs.FSDataOutputStream
import org.apache.hadoop.fs.FSDataInputStream

object HdfsUtils {

  private var conf = new Configuration()

  private var dfs = FileSystem.get(conf)

  def createFile(filePath: String): FSDataOutputStream = {
    var file = new File(filePath)
    return dfs.create(new Path(filePath))
  }

  def openFile(filePath: String): FSDataInputStream = {
    var file = new File(filePath)
    return dfs.open(new Path(filePath))
  }

  def writeFile(filePath: String, message: String): Unit = {
    var out = createFile(filePath)
    out.write(message.getBytes("utf-8"))
  }

}