package modelTest

import org.apache.bark._
import org.apache.bark.util._
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.StructType
import org.apache.spark.{SparkConf, SparkContext}
import org.scalatest.FunSuite

import scala.collection.mutable.ArrayBuffer
import org.scalatest.Assertions
import org.scalatest.Matchers
import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame, Row, SQLContext}
import java.io.{File, FileInputStream, FileOutputStream}

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import scala.util.matching.Regex
@RunWith(classOf[JUnitRunner])
class ValiTest extends FunSuite with Matchers {

  val dataFilePath = convertPath("data/test/dataFile/")
  val reqJsonPath = convertPath("data/test/reqJson/")
  val recordFilePath = convertPath("data/test/recordFile/")
  val recordFileName = "_RESULT_VALI"
  val dataDescFileSuffix = "_type"

  def convertPath(path: String) : String = {
    path.replace("/", System.getProperty("file.separator"))
  }

  def getValiTestReq() : ArrayBuffer[String] = {
    val jsonFiles = ArrayBuffer[String](reqJsonPath + "valiTest1.json", reqJsonPath + "valiTest2.json")
    jsonFiles
  }

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

  test("test validity file with srcFile") {
    val reqFiles = getValiTestReq()

    val conf = new SparkConf().setMaster("local[*]").setAppName("ValiTest")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)

    val mapper = new ObjectMapper()
    mapper.registerModule(DefaultScalaModule)

    var cnt = 1;

    val out = new FileOutputStream(recordFilePath + recordFileName)

    reqFiles.foreach { reqFile =>
      val input = new FileInputStream(reqFile)
      val configure = mapper.readValue(input, classOf[ValidityConfEntity])

      //source DataFrame
      val srcFile = dataFilePath + configure.dataSet
      val srcdf = loadDataFile(sqlContext, srcFile)

      //-- algorithm --
      Vali.calcVali(sc, configure, srcdf)

      //output
      out.write(("//" + "=" * 10).getBytes("utf-8"))
      out.write((s" $cnt. Test Validity model result with request file: $reqFile ").getBytes("utf-8"))
      out.write(("=" * 10 + "\n").getBytes("utf-8"))

      val rcd = configure.toString + "\n\n"
      out.write(rcd.getBytes("utf-8"))

      cnt += 1
    }

    out.close()

    sc.stop()
  }

//  test("load file") {
//    val conf = new SparkConf().setMaster("local[*]").setAppName("loadFile")
//    val sc = new SparkContext(conf)
//    if (sc.version.charAt(0) - '0' < 2) {
//      val sqlContext = new SQLContext(sc)
//
//      val file = "data/test/dataFile/testFile"
//      loadDataFile(sqlContext, file)
//    }
//  }

//  test("test clause") {
//    import org.apache.bark.common.PartitionPair
//    val partition1 = List[PartitionPair]()
//    val partition2 = List[PartitionPair](new PartitionPair("name", "vincent"))
//    val partition3 = List[PartitionPair](new PartitionPair("name", "vincent"), new PartitionPair("name", "lionel"), new PartitionPair("name", "shawn"))
//    val partitions1 = List[List[PartitionPair]]()
//    val partitions2 = List[List[PartitionPair]](partition1)
//    val partitions3 = List[List[PartitionPair]](partition2, partition3)
//
//    println(PartitionUtils.generateSourceSQLClause("table1", partition1))
//    println(PartitionUtils.generateSourceSQLClause("table1", partition2))
//    println(PartitionUtils.generateSourceSQLClause("table1", partition3))
//    println(PartitionUtils.generateTargetSQLClause("table2", partitions1))
//    println(PartitionUtils.generateTargetSQLClause("table2", partitions2))
//    println(PartitionUtils.generateTargetSQLClause("table2", partitions3))
//  }
}
