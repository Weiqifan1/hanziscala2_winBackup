package inpuSystemLookup.serialization

import inpuSystemLookup.dataClasses.{cedictMaps, codeToTextList, frequencyMaps, inputSystemCodeToInfoMap, inputSystemCombinedMap, inputSystemHanziToInfoMap, inputSystemTemp}
import inpuSystemLookup.imputMethodGenerator.inputMethodHandling
import inpuSystemLookup.imputMethodGenerator.inputMethodHandling.{createNestedSystemListHelper, generateInputSystemMapCode}
import inpuSystemLookup.serialization.FrequencyFileSerialization.{readCedictMapsFromFile, readJundaAndTzaiMapsFromFile}

import scala.io.Source
import java.io.{File, FileInputStream, FileOutputStream, ObjectInputStream, ObjectOutputStream, PrintWriter}

object InputSystemSerialization {

  def serializeInputSystems(): Unit ={

    val deserializedCedict: cedictMaps = readCedictMapsFromFile()
    val frequencyMap: frequencyMaps = readJundaAndTzaiMapsFromFile()

    val zhengma: codeToTextList = inputMethodHandling.createInputMethodObject(
      """\"[a-z]+\"=\".+""".r,
      ",",
      "=",
      true,
      "\"<>",
      "src/main/resources/hanzifilesRaw/zz201906_allcodes.txt")

    val hanziFirst: List[(String, codeToTextList)] =
      createNestedSystemListHelper(zhengma, false)

    val codeFirst: List[(String, codeToTextList)] =
      createNestedSystemListHelper(zhengma, true)

    val zhengmaAdvanced: inputSystemTemp = new inputSystemTemp(codeFirst, hanziFirst)

    //val inputSystemMap = generateInputSystemMap(zhengmaAdvanced, deserializedCedict, frequencyMap)
    val hanzi: inputSystemHanziToInfoMap = inputMethodHandling.generateInputSystemMapHanzi(zhengmaAdvanced, deserializedCedict, frequencyMap)

    //tempHanziSerialization
    val temphanzimap: Map[String, codeToTextList] = hanziFirst.map(i => i._1 -> i._2).toMap
    val code: inputSystemCodeToInfoMap = generateInputSystemMapCode(zhengmaAdvanced, deserializedCedict, frequencyMap, temphanzimap)

    val inputSystemMap: inputSystemCombinedMap = inputSystemCombinedMap(code, hanzi)

    serializeInputSystemWithJava(inputSystemMap)
  }

  //writeBinary((1, "omg", true))           ==> serializedTuple
  //readBinary[(Int, String, Boolean)](serializedTuple) ==> (1, "omg", true)

  def serializeInputSystemWithBinary(inputSystem: inputSystemCombinedMap): Unit = {
    //val binaryObj = upickle.default.writeBinary(inputSystem)
    //val dataOrFailure = serializeToArray(inputSystem: inputSystemCombinedMap)
    //val bengaluru = City("Bengaluru", "South Indian food", 12.97)

    /*implicit val cityRW = upickle.default.macroRW[inputSystemCombinedMap]
    val result = upickle.default.write(inputSystem)

    val outputFilePath = "src/main/resources/hanziFilesSerialized/zhengmaSerialized.txt"
    val pw = new PrintWriter(new File(outputFilePath ))
    pw.write(result)
    pw.close*/
  }

  def readInputSystemFromFileWithBinary(inputSystemFileName: String): inputSystemCombinedMap = {
    /*val inputFile = new File("src/main/resources/hanziFilesSerialized/" + inputSystemFileName)
    val fileContents = Source.fromFile(inputFile).getLines.mkString

    //read[(Int, String, Boolean)]("""[1,"omg",true]""") ==> (1, "omg", true)
    implicit val cityRW = upickle.default.macroRW[inputSystemCombinedMap]
    val output = read[inputSystemCombinedMap](fileContents)
    return output*/

    return null
  }

  def serializeInputSystemWithJava(inputSystem: inputSystemCombinedMap): Unit = {
    val outputFile = new File("src/main/resources/hanziFilesSerialized/zhengmaSerialized.txt")
    val fileOutputStream = new FileOutputStream(outputFile)
    val objectOutputStream = new ObjectOutputStream(fileOutputStream)
    objectOutputStream.writeObject(inputSystem)
    objectOutputStream.close()
  }

  def readInputSystemFromFileWithJava(inputSystemFileName: String): inputSystemCombinedMap = {
    val inputFile = new File("src/main/resources/hanziFilesSerialized/" + inputSystemFileName)
    val fileInputStream = new FileInputStream(inputFile)
    val objectInputStream = new ObjectInputStream(fileInputStream)
    val outputfrequencyMaps = objectInputStream.readObject().asInstanceOf[inputSystemCombinedMap]
    return outputfrequencyMaps
  }

}
