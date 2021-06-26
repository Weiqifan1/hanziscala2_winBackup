package ankiFileGenerator.frequencyFileHandling

import ankiFileGenerator.flashcardDataClasses.cedictFreqObject
import inpuSystemLookup.dataClasses.{cedictMaps, cedictObject, frequencyMaps}

object prepareTextHandlingMethods {

  def getListOfWordsFromText(text: String, traditional: Boolean, cedict: cedictMaps): List[String] ={
    if (text == null | text.isEmpty){return List("")}
    val chosenCedict: Map[String, List[cedictObject]] = if (traditional) cedict.traditionalMap else cedict.simplifiedMap
    if (!chosenCedict.get(text).isEmpty){
      return List(text)
    }else {
      val calculateWordList: (String, String, List[String]) = doGetWordList((text, text, List()), chosenCedict)
      val wordList = calculateWordList._3.filter(i => {!chosenCedict.get(i).isEmpty})
      return wordList
    }
  }

  def getNaiveInfoFromWord(word: String, traditional: Boolean, cedict: cedictMaps, frequency: frequencyMaps): cedictFreqObject = {
    val cedictMap: Map[String, List[cedictObject]] = if (traditional) {cedict.traditionalMap} else {cedict.simplifiedMap}
    val cedictResult: Option[List[cedictObject]] = cedictMap.get(word)

    val traditionalWord: List[String] = getStringsFromObject(cedictResult, true)
    val simplifiedWord: List[String] =  getStringsFromObject(cedictResult, false)
    val pinyin: List[String] = if (cedictResult.isEmpty) List("") else cedictResult.get.map(i => i.pinyin)
    val translation: List[String] = if (cedictResult.isEmpty) List("") else cedictResult.get.map(i => i.translation)
    val traditionalFreq: List[Int] = getHanziListFromText(traditionalWord).map(i =>
    {val optionFreq = frequency.traditional.get(i)
      if (optionFreq.isEmpty) 0 else optionFreq.get.toInt})
    val simplifiedFreq: List[Int] = getHanziListFromText(simplifiedWord).map(i =>
    {val optionFreq = frequency.simplified.get(i)
      if (optionFreq.isEmpty) 0 else optionFreq.get.toInt})

    val finalResult: cedictFreqObject =
      new cedictFreqObject(traditionalWord, simplifiedWord, pinyin, translation, traditionalFreq, simplifiedFreq)
    return finalResult
  }

  def getStringsFromObject(cedictResult: Option[List[cedictObject]], lookForTraditional: Boolean): List[String] = {
    var result: List[String] = List()
    if (cedictResult.isEmpty){
      result = List("")
    }else if(lookForTraditional) {
      result = cedictResult.get.map(i => i.traditionalHanzi)
    }else if(!lookForTraditional) {
      result = cedictResult.get.map(i => i.simplifiedHanzi)
    }
    return result
  }

  private def doGetWordList(tempTupple: (String, String, List[String]), chosenCedict: Map[String, List[cedictObject]]): (String, String, List[String]) ={
    val originalTextReconstructed: String = tempTupple._3.mkString
    if (tempTupple._2.equals(originalTextReconstructed)) { return (tempTupple._1, tempTupple._2, tempTupple._3)}
    else {
      //tuppleContent = 1: text with first word removed, 2: original string, 3: list with first word
      val resultOfFirstWord: (String, String, List[String]) = doGetFirstWord(tempTupple, chosenCedict)
      return doGetWordList(resultOfFirstWord, chosenCedict)
    }
  }

  private def doGetFirstWord(tempTupple: (String, String, List[String]), chosenCedict: Map[String, List[cedictObject]]): (String, String, List[String]) ={
    //tupple content = 1: shrinking string, 2: original String, 3: first word found
    if (!chosenCedict.get(tempTupple._1).isEmpty) {
      val lengthOfFirst: Int = tempTupple._1.length
      //remove up to first word
      val lengthOfFirstAndListWords: Int = lengthOfFirst + tempTupple._3.mkString.length
      val firstWordRemoved: String = tempTupple._2.substring(lengthOfFirstAndListWords)
      return (firstWordRemoved, tempTupple._2, tempTupple._3 :+ tempTupple._1)
    }else if (1 == tempTupple._1.length) {
      val lengthOfFirstAndListWords: Int = 1 + tempTupple._3.mkString.length
      val firstWordRemoved: String = tempTupple._2.substring(lengthOfFirstAndListWords)
      return (firstWordRemoved, tempTupple._2, tempTupple._3 :+ tempTupple._1)
    } else {
      val newShrinkingString: String = removeLastCharFromString(tempTupple._1)
      return doGetFirstWord((newShrinkingString, tempTupple._2, tempTupple._3), chosenCedict)
    }
  }

  private def removeLastCharFromString(text: String): String ={
    if (text == null | text == ""){return ""}
    val shortStringList: List[String] = getHanziListFromText(List(text)).dropRight(1)
    return shortStringList.mkString
  }

  private def getHanziListFromText(text: List[String]): List[String] = {
    val stream: List[Int] = text.map(i => i.codePoints.toArray.toList).flatten
    val backToString: List[String] = stream.map(i => Character.toChars(i).mkString)
    return backToString
  }

}
