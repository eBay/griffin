package com.ebay.bark

object PartitionUtils {

  def generateWhereClause(partition: List[PartitionConfig]): String = {
    val builder = StringBuilder.newBuilder

    for (i <- 0 to partition.length - 1) {

      if (i == 0) {
        builder.append(partition(i).colName)
        builder.append(" = ")
        builder.append(partition(i).colValue)
        builder.append(" ")
      } else {
        builder.append(" AND ")
        builder.append(partition(i).colName)
        builder.append(" = ")
        builder.append(partition(i).colValue)
        builder.append(" ")
      }

    }

    val where = if (partition.length > 0) " where " else " "

    where + builder.toString()

  }

  def generateTargetSQLClause(targetTable: String, partition: List[List[PartitionConfig]]): String = {
    val builder = StringBuilder.newBuilder

    val parts = if (partition.length == 0) List[PartitionConfig]() :: Nil else partition

    for (i <- 0 to parts.length - 1) {

      if (i == 0) {
        builder.append("SELECT * FROM ")
        builder.append(targetTable)
        builder.append(PartitionUtils.generateWhereClause(parts(i)))
      } else {
        builder.append(" UNION ALL ")
        builder.append("SELECT * FROM ")
        builder.append(targetTable)
        builder.append(PartitionUtils.generateWhereClause(parts(i)))
      }

    }

    builder.toString()

  }
}