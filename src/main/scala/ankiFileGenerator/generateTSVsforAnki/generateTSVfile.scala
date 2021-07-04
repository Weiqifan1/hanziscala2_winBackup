package ankiFileGenerator.generateTSVsforAnki

import ankiFileGenerator.flashcardDataClasses.{cedictFreqObject, flashcardLineObject, rawLineObject, storyObject}

import java.io.{File, PrintWriter}

object generateTSVfile {

  def writeTSVfile(storyToWrite: storyObject, title: String): Unit = {
    val writer = new PrintWriter(new File("outputFiles/" + title + ".tsv"))
    val textToWriteToFile: String = generateTSVText(storyToWrite)
    writer.write(textToWriteToFile)
    writer.close()
  }

  private def generateTSVText(storyToWrite: storyObject): String = {
    val flashcardLineObjectsList: List[flashcardLineObject] = storyToWrite.flashcardlineObjects
    val listOfCardRows: List[String] = generateCardRow(flashcardLineObjectsList)
    val finalText: String = listOfCardRows.mkString("\n")
    return finalText
  }

  private def generateCardRow(flashcardLineObjectsList: List[flashcardLineObject]): List[String] = {
    val listOfRows: List[String] = flashcardLineObjectsList.map(i => generateEachCard(i))
    return listOfRows
  }

  private def generateEachCard(eachCard: flashcardLineObject): String = {
    val hanziTextField: String = generateHanziTextFiled(eachCard.previousLineObj, eachCard.lineObj, eachCard.nextLineObject, eachCard.newCedictEntries)
    val pinyinTextField: String = generatePinyinTextField(eachCard.previousLineObj, eachCard.lineObj, eachCard.nextLineObject, eachCard.newCedictEntries)
    val totalInfoField: String = generateTotalInfoField(eachCard.previousLineObj, eachCard.lineObj, eachCard.nextLineObject, eachCard.newCedictEntries)
    return hanziTextField + "\t" + pinyinTextField + "\t" + totalInfoField
  }

  //***********************************************************************************************
  //                    hanzi field
  //***********************************************************************************************

  private def generateHanziTextFiled(previousLineObj: rawLineObject,
                                     lineObj: rawLineObject,
                                     nextLineObject: rawLineObject,
                                     newCedictEntries: List[cedictFreqObject]): String = {
    val newEntriesOnlyHanzi: String = generateNewEntriesOnlyHanzi(newCedictEntries)
    val currentLineOnlyHanzi: String = generateSubsectionWithOnlyHanzi(lineObj)
    val previousLineOnlyHanzi: String = generateSubsectionWithOnlyHanzi(previousLineObj)
    val nextLineOnlyHanzi: String = generateSubsectionWithOnlyHanzi(nextLineObject)
    val lineInfo: String = lineObj.lineInfo
    val storyInfo: String = lineObj.storyInfo1of2 + "\n" + lineObj.storyInfo2of2
    return "new entries:" + "\n" + newEntriesOnlyHanzi + "\n\n" +
      "current line:" + "\n" + currentLineOnlyHanzi + "\n\n" +
      "previous line:" + "\n" + previousLineOnlyHanzi + "\n\n" +
      "next line:" + "\n" + nextLineOnlyHanzi + "\n\n" +
      "lineInfo: " + "\n" + lineInfo + "\n" +
      "story info:" + "\n" + storyInfo
  }

  private def generateNewEntriesOnlyHanzi(someLineObj: List[cedictFreqObject]): String = {
    val tradHanzi: String = someLineObj.map(i => i.traditionalHanzi.mkString("_")).mkString(" * ")
    val simpHanzi: String = someLineObj.map(i => i.simplifiedHanzi.mkString("_")).mkString(" * ")
    return "traditional: " + tradHanzi + "\n" +
      "simplified: " + simpHanzi
  }

  private def generateSubsectionWithOnlyHanzi(lineObj: rawLineObject): String = {
    val originalLine: String = lineObj.originallLine
    val traditional: String = lineObj.cedictEntries.map(i => i.traditionalHanzi(0)).mkString(" * ")
    val simplified: String = lineObj.cedictEntries.map(i => i.simplifiedHanzi(0)).mkString(" * ")
    val finalText: String = "original: " + originalLine + "\n" +
      "traditional: " + traditional + "\n" +
      "simplified: " + simplified
    return finalText
  }


  //***********************************************************************************************
  //                    pinyin field
  //***********************************************************************************************
  private def generatePinyinTextField(previousLineObj: rawLineObject,
                                      lineObj: rawLineObject,
                                      nextLineObject: rawLineObject,
                                      newCedictEntries: List[cedictFreqObject]): String = {
    val newEntriesOnlyPinyin: String = generateNewEntriesOnlyPinyin(newCedictEntries)
    val currentLineOnlyPinyin: String = generateSubsectionWithOnlyPinyin(lineObj)
    val previousLineOnlyPinyin: String = generateSubsectionWithOnlyPinyin(previousLineObj)
    val nextLineOnlyPinyin: String = generateSubsectionWithOnlyPinyin(nextLineObject)
    val lineInfo: String = lineObj.lineInfo
    val storyInfo: String = lineObj.storyInfo1of2 + "\n" + lineObj.storyInfo2of2
    return "new entries:" + "\n" + newEntriesOnlyPinyin + "\n\n" +
      "current line:" + "\n" + currentLineOnlyPinyin + "\n\n" +
      "previous line:" + "\n" + previousLineOnlyPinyin + "\n\n" +
      "next line:" + "\n" + nextLineOnlyPinyin + "\n\n" +
      "lineInfo: " + "\n" + lineInfo + "\n" +
      "story info:" + "\n" + storyInfo
  }

  private def generateNewEntriesOnlyPinyin(newCedictEntries: List[cedictFreqObject]): String = {
    val eachEntry: List[String] = newCedictEntries.map(i => i.pinyin.mkString("_"))
    val joinedEntries: String = eachEntry.mkString(" * ")
    return joinedEntries
  }

  private def generateSubsectionWithOnlyPinyin(someLineObj: rawLineObject): String = {
    val pinyin: String = createPinyinLineFromEntries(someLineObj.cedictEntries)
    return pinyin
  }


  //***********************************************************************************************
  //                    total info fields
  //***********************************************************************************************

  private def generateTotalInfoField(previousLineObj: rawLineObject,
                                     lineObj: rawLineObject,
                                     nextLineObject: rawLineObject,
                                     newCedictEntries: List[cedictFreqObject]): String = {
    val generateNewCedictEntriesField: String = generateNewEntriesWithAllInfo(newCedictEntries)
    val generateCurrentLineField: String = generateSubsectionWithAllInfo(lineObj)
    val generatePreviousLineField: String = generateSubsectionWithAllInfo(previousLineObj)
    val generateNextLineField: String = generateSubsectionWithAllInfo(nextLineObject)
    val lineInfo: String = lineObj.lineInfo
    val storyInfo: String = lineObj.storyInfo1of2 + "\n" + lineObj.storyInfo2of2
    return "new entries:" + "\n" + generateNewCedictEntriesField + "\n\n" +
      "current line:" + "\n" + generateCurrentLineField + "\n\n" +
      "previous line:" + "\n" + generatePreviousLineField + "\n\n" +
      "next line:" + "\n" + generateNextLineField + "\n\n" +
      "lineInfo: " + "\n" + lineInfo + "\n" +
      "story info:" + "\n" + storyInfo
  }

  private def generateNewEntriesWithAllInfo(newCedictEntries: List[cedictFreqObject]): String = {
    val eachEntry: List[String] = newCedictEntries.map(i => singleEntryFullInfo(i))
    val joinedEntries: String = eachEntry.mkString("\n")
    return joinedEntries
  }

  private def generateSubsectionWithAllInfo(someLineObj: rawLineObject): String = {
    val originalLine: String = someLineObj.originallLine
    val pinyin: String = createPinyinLineFromEntries(someLineObj.cedictEntries)
    val translationLines: String = createEntryFullInfoLines(someLineObj.cedictEntries)
    return originalLine + "\n" + pinyin + "\n" + translationLines
  }

  private def createPinyinLineFromEntries(cedictEntries: List[cedictFreqObject]): String = {
    val allPintins: String = cedictEntries.map(i => i.pinyin.mkString("_")).mkString(" * ")
    return allPintins
  }

  private def createEntryFullInfoLines(cedictEntries: List[cedictFreqObject]): String = {
    val entries: List[String] = cedictEntries.map(i => singleEntryFullInfo(i))
    val entriesAddedTogether: String = entries.mkString("\n")
    return entriesAddedTogether
  }

  private def singleEntryFullInfo(singleEntry: cedictFreqObject): String = {
    val chineseWordTraditional: String = singleEntry.traditionalHanzi.mkString("*")
    val chineseWordSimplified: String = singleEntry.simplifiedHanzi.mkString("*")
    val pinyin: String = singleEntry.pinyin.mkString("_")
    val traditionaFrequency: String = singleEntry.traditionalFrequency.mkString(" ")
    val simplifiedFrequency: String = singleEntry.simplifiedFrequency.mkString(" ")
    val translations: String = singleEntry.translation.mkString(" ")
    val result: String =
      chineseWordTraditional + " " +
        traditionaFrequency + " " +
        chineseWordSimplified + " " +
        simplifiedFrequency + " " +
        pinyin + " " +
        translations
    return result
  }
}
