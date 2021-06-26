package inpuSystemLookup.services


import inpuSystemLookup.dataClasses.{cedictObject, inputSystemCombinedMap, inputSystemHanziInfo, inputSystemHanziInfoList}
import inpuSystemLookup.serialization.InputSystemSerialization.readInputSystemFromFileWithJava

import scala.collection.mutable.ListBuffer
import scala.math.Ordering.Implicits.seqOrdering

object inputMethodService {

  def printableCodeListResults(codes: List[String], inputSystem: inputSystemCombinedMap): String ={
    //we need to reverse the order for printing, since we want the most common characters to be at the bottom
    val hanziInfoList: List[inputSystemHanziInfo] = getSortedInfoListsFromCodes(codes, inputSystem).reverse
    var output = printInfo(hanziInfoList)
    return output
  }

  def printableHanziTextListResults(hanzi: String, inputSystem: inputSystemCombinedMap): String ={
    //we need to reverse the order for printing, since we want the most common characters to be at the bottom
    val hanziInfoList: List[inputSystemHanziInfo] = getSortedInfoListsFromHanzi(hanzi, inputSystem).reverse
    var output = printInfo(hanziInfoList)
    return output
  }

  def runConsoleProgram(singleInputSystem: inputSystemCombinedMap): Unit ={
    var userInput = ""
    while (!userInput.equals("quit")) {
      userInput = scala.io.StdIn.readLine("writeInput ")
      val smallLetters: String = userInput.toLowerCase()
      consoleProgramIteration(smallLetters, singleInputSystem)
    }
    println("user has ended the program")
  }

  //TODO: til nedenst[ende metode skal der tilf'jes en metode til at generere en liste af koder

  private def consoleProgramIteration(userInput: String, singleInputSystem: inputSystemCombinedMap): Unit = {

    val isCode: Boolean = inputIsCode(userInput)
    if (isCode){
      val codeList: List[String] = generateCodeListFromInput(userInput)
      val resultString = printableCodeListResults(codeList, singleInputSystem)
      println(resultString)
    }else {
      val resultString = printableHanziTextListResults(userInput, singleInputSystem)
      println(resultString)
    }
  }

  def generateCodeListFromInput(inputCode: String): List[String] = {
    val numberOfPlaceholders = inputCode.filter(_ == '*').length
    val placeholderLetter: List[String] = ('a' to 'z').map(i => i.toString).toList
    if (numberOfPlaceholders == 0) {
      return List(inputCode)
    } else {
      val indexoffirstPlaceholder = inputCode.indexOf('*')
      val splitPlaceholder = inputCode.splitAt(indexoffirstPlaceholder)
      val listEdition =  List(splitPlaceholder._1, splitPlaceholder._2.substring(1))
      val listOfNewVariations: List[String] = placeholderLetter.map(i => listEdition(0) + i + listEdition(1))
      val result = listOfNewVariations.map(i => generateCodeListFromInput(i)).flatten
      return result
    }
  }

  def inputIsCode(userInput: String): Boolean = {
    var ordinary: Set[Char] = (('a' to 'z') ++ ('A' to 'Z') ++ ('0' to '9')).toSet
    ordinary += '*'
    val result = userInput.forall(ordinary.contains(_))
    return result
  }

  private def printInfo(hanziInfoList: List[inputSystemHanziInfo]): String = {
    var output = ""
    for (eachInfo: inputSystemHanziInfo <- hanziInfoList) {
      val eachPrint: String = getStringFromHanziInfo(eachInfo)
      output += eachPrint + "\n"
    }
    return output
  }

  def getSortedInfoListsFromCodes(codes: List[String], inputSystem: inputSystemCombinedMap): List[inputSystemHanziInfo] = {
    val infoList: List[inputSystemHanziInfo] = codes.map(i => getInfoListFromCode(i, inputSystem)).flatten
    if (infoList.isEmpty) return List()
    val removeDublicates = removeInfoWithSameCharacter(infoList)
    val sorted = removeDublicates.sorted
    return sorted
  }

  private def getSortedInfoListsFromHanzi(hanzi: String, inputSystem: inputSystemCombinedMap): List[inputSystemHanziInfo] = {
    val lookup: Option[inputSystemHanziInfoList] = inputSystem.hanziToInfo.content.get(hanzi)
    if (lookup.isEmpty) return List() //else return lookup.get.content
    val removeDublicates = removeInfoWithSameCharacter(lookup.get.content)
    val sorted = removeDublicates.sorted
    return sorted
  }

  private def getInfoListFromCode(code: String, inputSystem: inputSystemCombinedMap): List[inputSystemHanziInfo] ={
    val lookup: Option[inputSystemHanziInfoList] = inputSystem.codeToInfo.content.get(code)
    if (lookup.isEmpty) return List() else return lookup.get.content
  }

  private def getStringFromHanziInfo(hanziInfo: inputSystemHanziInfo): String = {
    var output = ""
    val primaryHanzi: String = hanziInfo.hanzi.toString
    val codes: String = getCodes(hanziInfo)
    val frequency = "tradFreq:" + hanziInfo.traditionalFrequency.toString() +
                    " simpFreq:" + hanziInfo.simplifiedFrequency.toString()
    val cedictLines: String = getCedictLines(hanziInfo)
    val unicode: String = primaryHanzi.codePoints.toArray.toList.toString()

    output = primaryHanzi  +
             " " + codes +
             " " + frequency +
             " unicode:" + unicode +
             "\n\t" + cedictLines
    return output.trim
  }


  private def getCodes(hanziInfo: inputSystemHanziInfo): String ={
    val codeList = hanziInfo.codes
    var outputString = ""
    for (eachCode <- codeList) {
      outputString += eachCode + " "
    }
    outputString = "[" + outputString.trim + "]"
    return outputString
  }

  private def getCedictLines(hanziInfo: inputSystemHanziInfo): String ={
    val traditionalCedict: List[cedictObject] = if (!hanziInfo.cedictTrad.isEmpty) hanziInfo.cedictTrad.get else List()
    val simplifiedCedict: List[cedictObject] = if (!hanziInfo.cedictSimp.isEmpty) hanziInfo.cedictSimp.get else List()

    var textOutput: String = "\n\t" + "Trad: "
    for (cedictElem <- traditionalCedict) {
      textOutput += "\t" + cedictElem.traditionalHanzi +
                        " " + cedictElem.simplifiedHanzi +
                        " " + cedictElem.pinyin +
                        " " + cedictElem.translation + "\n"
    }
    textOutput += "\t" + "Simp: "
    for (cedictElem <- simplifiedCedict) {
      textOutput += "\t" + cedictElem.traditionalHanzi +
        " " + cedictElem.simplifiedHanzi +
        " " + cedictElem.pinyin +
        " " + cedictElem.translation + "\n"
    }
    return textOutput.trim
  }

  private def loadData(): (inputSystemCombinedMap) = {
    val zhengma = readInputSystemFromFileWithJava("zhengmaSerialized.txt")
    return (zhengma)
  }

  private def removeInfoWithSameCharacter(nestdeInfo: List[inputSystemHanziInfo]): List[inputSystemHanziInfo] = {
    var unique: ListBuffer[inputSystemHanziInfo] = ListBuffer()
    var listOfTexts: ListBuffer[String] = ListBuffer()
    for (info: inputSystemHanziInfo <- nestdeInfo) {
      val chineseText = info.hanzi
      if (!listOfTexts.contains(chineseText)) {
        unique.addOne(info)
      }
      listOfTexts.addOne(chineseText)
    }
    return unique.toList
  }



}
