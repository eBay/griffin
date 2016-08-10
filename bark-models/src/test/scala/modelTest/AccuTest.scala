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
@RunWith(classOf[JUnitRunner])
class AccuTest extends FunSuite with Matchers {

  val dataFilePath = convertPath("data/test/dataFile/")
  val reqJsonPath = convertPath("data/test/reqJson/")
  val recordFilePath = convertPath("data/test/recordFile/")
  val recordFileName = "_RESULT_ACCU"
  val dataDescFileSuffix = "_type"

  def convertPath(path: String) : String = {
    path.replace("/", System.getProperty("file.separator"))
  }

  def getAccTestReq() : ArrayBuffer[String] = {
    val jsonFiles = ArrayBuffer[String](reqJsonPath + "accTest1.json", reqJsonPath + "accTest2.json", reqJsonPath + "accTest3.json")
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

  test("test accuracy file with srcFile and tgtFile") {
    val reqFiles = getAccTestReq()

    val conf = new SparkConf().setMaster("local[*]").setAppName("AccTest")
    val sc = new SparkContext(conf)
    if (sc.version.charAt(0) - '0' < 2) {
      val sqlContext = new SQLContext(sc)

      val mapper = new ObjectMapper()
      mapper.registerModule(DefaultScalaModule)

      var cnt = 1;

      val out = new FileOutputStream(recordFilePath + recordFileName)

      reqFiles.foreach { reqFile =>
        val input = new FileInputStream(reqFile)
        val configure = mapper.readValue(input, classOf[AccuracyConfEntity])

        //source DataFrame
        val srcFile = dataFilePath + configure.source
        val srcdf = loadDataFile(sqlContext, srcFile)

        //target DataFrame
        val tgtFile = dataFilePath + configure.target
        val tgtdf = loadDataFile(sqlContext, tgtFile)

        //-- algorithm --
        val ((missCount, srcCount), missedList) = Accu.calcAccu(sc, configure, srcdf, tgtdf)

        //output
        out.write(("//" + "=" * 10).getBytes("utf-8"))
        out.write((s" $cnt.. Test Accuracy model result with request file: $reqFile ").getBytes("utf-8"))
        out.write(("=" * 10 + "\n").getBytes("utf-8"))

        val rslt = s"match percentage: ${((1 - missCount.toDouble / srcCount) * 100)} %"
        val rcds = missedList.mkString("\n")
        val rcd = rslt + "\n\n" + rcds + "\n\n";

        out.write(rcd.getBytes("utf-8"))

        cnt += 1
      }

      out.close()

      sc.stop()
    }
  }
}
