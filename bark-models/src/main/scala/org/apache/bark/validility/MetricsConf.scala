package org.apache.bark.validility

object MetricsConf {
  val defaultCount = 0
  val totalCount = 1
  val nullCount = 2
  val uniqueCount = 3
  val duplicateCount = 4
  val maximum = 5
  val minimum = 6
  val mean = 7
  val median = 8
  val regularExp = 9
  val patternFreq = 10
}

object MetricsType extends Enumeration{
  type WeekDay = Value
  val DefaultCount, TotalCount, NullCount, UniqueCount, DuplicateCount, Maximum, Minimum,Mean,Median,RegularExp,PatternFreq = Value
}

//object Main extends App {
//
//  import MetricsType._
//  MetricsType.values foreach(x => println(x.id))
//}