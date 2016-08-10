package modelTest

import java.io.File

import org.apache.bark.util.DataTypeUtils
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.types.{StructField, StructType}

object FileLoaderUtil {
  val dataDescFileSuffix = "_type"

  def loadDataFile(sqlContext: SQLContext, file: String, fmt: String = "com.databricks.spark.csv") = {
    val reader = sqlContext.read
      .format(fmt)
      .option("header", "true")
      .option("treatEmptyValuesAsNulls", "true")
      .option("delimiter", ",")

    val typeFile = file + dataDescFileSuffix
    val typeExist = new File(typeFile).exists()
    val schemaReader = if (typeExist) {
      val types = reader.load(typeFile).collect.map( r => (r.getString(0), r.getString(1)) )
      val fields = types.map(kt => StructField(kt._1, DataTypeUtils.str2DataType(kt._2)))
      val customSchema = StructType(fields)
      reader.schema(customSchema)
    } else {
      reader.option("inferSchema", "true")
    }

    schemaReader.load(file)
  }

  def convertPath(path: String) : String = {
    path.replace("/", System.getProperty("file.separator"))
  }
}
