package inpuSystemLookup

import dataClasses.{codeToTextList, inputSystemCombinedMap, inputSystemHanziInfo, inputSystemHanziInfoList}
import serialization.FrequencyFileSerialization.serializeCedictAndFrequencyFiles
import serialization.InputSystemSerialization.{readInputSystemFromFileWithJava, serializeInputSystems}
import services.inputMethodService.{generateCodeListFromInput, getSortedInfoListsFromCodes, runConsoleProgram}
import testPreparation.hashmapTestPrepare.{listOf5000Simplified, listOf5000Traditional}

import scala.collection.mutable.ListBuffer

object Boundary {

  def runInputSystemLookup(): Unit = {
    println("hej lykke")

    //serializeCedictAndFrequencyFiles()
    //serializeInputSystems()

    //val printing: String = printableCodeListResults(List("zz","ab", "aa", "aavv", "psli", "klg", "boji"), zhengma)
    //println(printing)

    //*********** run console program
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    val zhengma = readInputSystemFromFileWithJava("zhengmaSerialized.txt")
    runConsoleProgram(zhengma)

    //************* run api data:
    //val result = apiMethod("sd", zhengma)


    println("farvel lykke ")
  }
/*
  def apiMethod(input: String, inputSystem: inputSystemCombinedMap): String ={
    val smallLetters: String = input.toLowerCase()
    val codeList: List[String] = generateCodeListFromInput(smallLetters)
    val hanziInfoList: List[inputSystemHanziInfo] = getSortedInfoListsFromCodes(codeList, inputSystem)
    val outpuObject: inputSystemHanziInfoList = inputSystemHanziInfoList(hanziInfoList)
    // create a JSON string from the Person, then print it
    implicit val formats = DefaultFormats
    val jsonString = write(outpuObject)
    return jsonString
  }*/

  def qualityCheckInput(input: codeToTextList): (Integer, Integer) = {
    val systemCharacterStrings: Predef.Set[String] = input.content.map(i => i.hanzi).toSet
    val jundacharacters = listOf5000Simplified()//scala.io.Source.fromFile("src/main/resources/frequencyfilesRaw/testJunda.txt").mkString.split("")
    val tzaicharacters = listOf5000Traditional()

    var missingJunda = new ListBuffer[String]()
    for (character: String <- jundacharacters) {
      val existInSet: Boolean = systemCharacterStrings.contains(character)
      if (!existInSet){
        missingJunda.addOne(character)
      }
    }

    var missingTzai = new ListBuffer[String]()
    for (character: String <- tzaicharacters) {
      val existInSet: Boolean = systemCharacterStrings.contains(character)
      if (!existInSet){
        missingTzai.addOne(character)
      }
    }

    return (missingJunda.length, missingTzai.length)
  }
}
