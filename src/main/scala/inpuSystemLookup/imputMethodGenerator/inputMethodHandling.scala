package inpuSystemLookup.imputMethodGenerator

import inpuSystemLookup.dataClasses.{cedictMaps, cedictObject, codeToMultipleTextsList, codeToMultipleTextsObject, codeToTextList, codeToTextObject, frequencyMaps, inputSystemCodeToInfoMap, inputSystemHanziInfo, inputSystemHanziInfoList, inputSystemHanziToInfoMap, inputSystemTemp, textToMultipleCodesList, textToMultipleCodesObject}

import java.util.stream.IntStream
import scala.collection.mutable.ListBuffer
import scala.util.matching.Regex

object inputMethodHandling {

  /*
  case class inputSystemHanziInfo(hanzi: String,
                                  traditional: Boolean,
                                  simplified: Boolean,
                                  code: String,
                                  pronounciation: String,
                                  translation: String,
                                  traditionalFrequency: List[Int],
                                  simplifiedFrequency: List[Int])*/
/*
  def createInputSystemMap(inputSystem: codeToTextList): Unit ={

    val zhengmaAdvanced: inputSystemTemp = createNestedInputSystemListTupple(inputSystem)

    val codeToList = zhengmaAdvanced.codeToList
    val hanziToList = zhengmaAdvanced.hanziToList


  }
*/


  def createNestedSystemListHelper(inputSystem: codeToTextList, codeFirst: Boolean): List[(String, codeToTextList)] = {
    val codeSorted = if (codeFirst) {inputSystem.content.sortBy(_.code)} else {inputSystem.content.sortBy(_.hanzi)}
    var codeToHanziList: ListBuffer[(String, codeToTextList)] = new ListBuffer[(String, codeToTextList)]
    var tempList: ListBuffer[codeToTextObject] = new ListBuffer[codeToTextObject]
    var previousCode: String = if (codeFirst) {codeSorted(0).code} else {codeSorted(0).hanzi}

    val lastCode: String = if (codeFirst) {codeSorted(codeSorted.length - 1).code} else {codeSorted(codeSorted.length - 1).hanzi}
    var last: ListBuffer[codeToTextObject] = new ListBuffer[codeToTextObject]
    for (eachObj: codeToTextObject <- codeSorted) {
      val currentCode = if (codeFirst) {eachObj.code} else {eachObj.hanzi}
      if (currentCode.equals(lastCode)) {
        val newCodeToTextTuple: (String, codeToTextList) = (previousCode, codeToTextList(tempList.toSet.toList))
        codeToHanziList += newCodeToTextTuple
        last += eachObj
      } else if (currentCode.equals(previousCode)) {
        tempList += eachObj
      } else {
        val newCodeToTextTuple: (String, codeToTextList) = (previousCode, codeToTextList(tempList.toSet.toList))
        codeToHanziList += newCodeToTextTuple
        previousCode = currentCode
        tempList = new ListBuffer[codeToTextObject]
        tempList += eachObj
      }
    }
    val finalCodeToTextTupple: (String, codeToTextList) = (lastCode, codeToTextList(last.toSet.toList))
    codeToHanziList += finalCodeToTextTupple

    val result = codeToHanziList.toList
    return result
  }

  def createTextToMultipleCodes(input: codeToTextList): textToMultipleCodesList = {
    val listOfObject: List[codeToTextObject] = input.content
    val finalObjectBuffer = new ListBuffer[textToMultipleCodesObject]()

    for (eachObj: codeToTextObject <- listOfObject){
      val chineseText = eachObj.hanzi
      val codes = new ListBuffer[String]()
      codes += eachObj.code
      for (subObj: codeToTextObject <- listOfObject){
        if (subObj.hanzi.equals(chineseText)){
          codes += subObj.code
        }
      }
      val actualList = codes.toSet.toList.sorted
      val newObj = textToMultipleCodesObject(chineseText, actualList)
      finalObjectBuffer += newObj
    }

    val finalContent = finalObjectBuffer.toSet.toList
    val returnobject = textToMultipleCodesList(finalContent)
    return returnobject
  }

  def createCodeToMultipleTexts(input: codeToTextList): codeToMultipleTextsList = {
    val listOfObject: List[codeToTextObject] = input.content
    val finalObjectBuffer = new ListBuffer[codeToMultipleTextsObject]()

    for (eachObj: codeToTextObject <- listOfObject){
      val currentcode = eachObj.code
      val chineseTexts = new ListBuffer[String]()
      chineseTexts += eachObj.hanzi
      for (subObj: codeToTextObject <- listOfObject){
        if(subObj.code.equals(currentcode)){
          chineseTexts += subObj.hanzi
        }
      }
      val actualList = chineseTexts.toSet.toList.sorted
      val newObj = codeToMultipleTextsObject(currentcode, actualList)
      finalObjectBuffer += newObj
    }

    val finalContent = finalObjectBuffer.toSet.toList
    val returnObject = codeToMultipleTextsList(finalContent)
    return returnObject
  }

  def createInputMethodObject(lineRegex: Regex,
                              splitLine: String,
                              splitCodeAndText: String,
                              codeFirst: Boolean,
                              removeChars: String,
                              filepath: String): codeToTextList ={
    //val regex: Regex = """\"[a-z]+\"=\".+""".r//zhengma.lineMatchRegexList()
    //remove byteordermark if there are any:
    val rawInputFromFile: String = scala.io.Source.fromFile(filepath).mkString
    val hanzilines: List[String] = rawInputFromFile.split("\n").toList
    //scala.io.Source.fromFile("src/main/resources/hanzifilesRaw/zz201906_test.txt").mkString.split("\n").toList
    val charsToRemoveSet = removeChars.toSet//"\"<>".toSet

    var myobjects: List[codeToTextObject] = List()
    for (line: String <- hanzilines){
      val matching = lineRegex findFirstIn line
      if (!None.equals(matching)){
        val listOf1ormany: List[String] = line.split(splitLine).toList//eachMatch.source.toString.split("=").toList
        for (part <- listOf1ormany){
          val subListOf2 = part.split(splitCodeAndText).toList
          if (codeFirst){
            val firstStr: String = subListOf2(0).filterNot(charsToRemoveSet).trim
            val secondStr: String = subListOf2(1).filterNot(charsToRemoveSet).trim
            val codeToText = codeToTextObject(firstStr, secondStr)
            myobjects ::= codeToText
          }else {
            val firstStr: String = subListOf2(1).filterNot(charsToRemoveSet).trim
            val secondStr: String = subListOf2(0).filterNot(charsToRemoveSet).trim
            val codeToText = codeToTextObject(firstStr, secondStr)
            myobjects ::= codeToText
          }
        }
      }
    }
    val inputSystem = codeToTextList(myobjects)
    return inputSystem
  }


  /* inputSystemHanziInfo     (hanzi: String,
                                code: String,
                                cedict: cedictObject,
                                traditionalFrequency: List[Int],
                                simplifiedFrequency: List[Int])*/


  def getCodeListFromtempMap(codeList: codeToTextList): List[String] ={
    var li_buffer: List[String] = codeList.content.map{i => i.code}.toSet.toList
    return li_buffer
  }

  def generateInputSystemMapCode(inputSystem: inputSystemTemp, cedict: cedictMaps, frequency: frequencyMaps, temphanzimap: Map[String, codeToTextList]): inputSystemCodeToInfoMap = {
    val inputSystemCodeContent: List[(String, inputSystemHanziInfoList)]  =
      inputSystem.codeToList.map(i => {
        val code: String = i._1
        val subcontent: List[codeToTextObject] = i._2.content
        //getCodeListFromtempMap(temphanzimap.get(code).get)
        val codes: List[String] =  subcontent.map(cTot => cTot.code).toSet.toList
        val listOfStrings: List[inputSystemHanziInfo] = subcontent.map(k => {
          val hanzi: String = k.hanzi
          //val code: String = k.code
          val cedictSimp: Option[List[cedictObject]] = cedict.simplifiedMap.get(hanzi)
          val cedictTrad: Option[List[cedictObject]] = cedict.traditionalMap.get(hanzi)
          val traditionalFrequency: List[String] = frequencyInfoTraditionalFromString(hanzi, frequency)
          val simplifiedFrequency: List[String] = frequencyInfoSimplifiedFromString(hanzi, frequency)
          inputSystemHanziInfo(hanzi, getCodeListFromtempMap(temphanzimap.get(hanzi).get), cedictSimp, cedictTrad, traditionalFrequency, simplifiedFrequency)
        })
        val infoList = inputSystemHanziInfoList(listOfStrings)
        (code, infoList)
      })

    //finalSimp.map(tup => tup.tuple._1 -> tup.tuple._2).toMap
    val codeMap: Map[String, inputSystemHanziInfoList] = inputSystemCodeContent.map(i => i._1 -> i._2).toMap
    val codeMapObj: inputSystemCodeToInfoMap = inputSystemCodeToInfoMap(codeMap)

    return codeMapObj
  }

  def generateInputSystemMapHanzi(inputSystem: inputSystemTemp, cedict: cedictMaps, frequency: frequencyMaps): inputSystemHanziToInfoMap = {
    val inputSystemHanziContent: List[(String, inputSystemHanziInfoList)]  =
      inputSystem.hanziToList.map(i => {
        val hanzi: String = i._1
        val subcontent: List[codeToTextObject] = i._2.content
        val codes: List[String] =  subcontent.map(cTot => cTot.code).toSet.toList
        val listOfStrings: List[inputSystemHanziInfo] = subcontent.map(k => {
          val hanzi: String = k.hanzi
          //val code: String = k.code
          val cedictSimp: Option[List[cedictObject]] = cedict.simplifiedMap.get(hanzi)
          val cedictTrad: Option[List[cedictObject]] = cedict.traditionalMap.get(hanzi)
          val traditionalFrequency: List[String] = frequencyInfoTraditionalFromString(hanzi, frequency)
          val simplifiedFrequency: List[String] = frequencyInfoSimplifiedFromString(hanzi, frequency)
          inputSystemHanziInfo(hanzi, codes, cedictSimp, cedictTrad, traditionalFrequency, simplifiedFrequency)
        })
        val infoList = inputSystemHanziInfoList(listOfStrings)
        (hanzi, infoList)
      })

    val hanziMap: Map[String, inputSystemHanziInfoList] = inputSystemHanziContent.map(i => i._1 -> i._2).toMap
    val hanziMapObj: inputSystemHanziToInfoMap = inputSystemHanziToInfoMap(hanziMap)

    return hanziMapObj
  }

  def frequencyInfoTraditionalFromString(inputHanziString: String, frequency: frequencyMaps): List[String] = {
    val stream: List[Int] = inputHanziString.codePoints.toArray.toList
    val backToString: List[String] = stream.map(i => Character.toChars(i).mkString)
    val traditional: List[String] = backToString.map(i => {
      val lookupResult: Option[String] = frequency.traditional.get(i)
      if (lookupResult.nonEmpty){lookupResult.get.trim}else{"0"}
    })
    return traditional
  }

  def frequencyInfoSimplifiedFromString(inputHanziString: String, frequency: frequencyMaps): List[String] = {
    val stream: List[Int] = inputHanziString.codePoints.toArray.toList
    val backToString: List[String] = stream.map(i => Character.toChars(i).mkString)
    val simplified: List[String] = backToString.map(i => {
      val lookupResult: Option[String] = frequency.simplified.get(i)
      if (lookupResult.nonEmpty){lookupResult.get.trim}else{"0"}
    })
    return simplified
  }


}


/*
  def createInputMethodMap(inputList: codeToTextList, cedict: cedictMaps, frequencyMaps: frequencyMaps): inputSystemCombinedMap ={
    var mapOfObjects: ListBuffer[inputSystemHanziInfo] = new ListBuffer[inputSystemHanziInfo]()
    for (eachMapping: codeToTextObject <- inputList.content){
      val hanzi = eachMapping.hanzi
      val code = eachMapping.code
      val cedictSimpObjects: List[cedictObject] = cedict.simplifiedMap(hanzi)
      val cedictTradObjects: List[cedictObject] = cedict.traditionalMap(hanzi)
      val simp = if (cedictSimpObjects.nonEmpty) {true} else {false}
      val trad = if (cedictTradObjects.nonEmpty) {true} else {false}

    }
    return null
  }

  //                                hanzi: String,
  //                                traditional: Boolean,
  //                                simplified: Boolean,
  //                                code: String,
  //                                pronounciation: String,
  //                                translation: String,
  //                                traditionalFrequency: List[Int],
  //                                simplifiedFrequency: List[Int]*/
