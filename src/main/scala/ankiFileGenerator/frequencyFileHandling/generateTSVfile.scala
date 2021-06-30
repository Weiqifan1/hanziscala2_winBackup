package ankiFileGenerator.frequencyFileHandling

import ankiFileGenerator.flashcardDataClasses.{cedictFreqObject, rawLineObject, storyObject}
import ankiFileGenerator.frequencyFileHandling.generateStoryObject.{getListOfWordsFromText, getNaiveInfoFromWord}
import inpuSystemLookup.dataClasses.{cedictMaps, frequencyMaps}

import java.io.{File, PrintWriter}

object generateTSVfile {

  def writeTSVfile(textToWrite: String, title: String): Unit ={
    val writer = new PrintWriter(new File("outputFiles/" + title + ".tsv"))
    writer.write(textToWrite)
    writer.close()
  }

  //it is asume that the first 2 lines contain story info,
  //the lines after that contain the lines and there info,
  //separated by blank lines, eg:
  //[story title]
  //[more story info]
  //<blank>
  //[line info]
  //[line]
  //<blank>...
  def parseTextFileAsRawLineList(
      storyFileContent: String,
      traditional: Boolean,
      cedict: cedictMaps,
      frequency: frequencyMaps): List[rawLineObject] = {
    val regexToUse: String = "\n[\\s]*\n"
    val parsingResult: List[String] = storyFileContent.split(regexToUse).toList
    val storyInfoRaw: List[String] = parsingResult(0).split("\n").toList
    val storyInfo1of2: String = storyInfoRaw(0).trim
    val storyInfo2of2: String = if (storyInfoRaw.length > 1){storyInfoRaw(1).trim}else{""}
    val lines: List[rawLineObject] = parsingResult.drop(1).map(i =>{
      generateLineObject(traditional, cedict, frequency, storyInfo1of2, storyInfo2of2, i)
    })
    return lines
  }

  private def removeNewLineCharactersFromTest(lines: List[String]): String = {
    val newlinesRemove: List[String] = lines.map(i => i.replaceAll("[\r\n]+", " ").trim)
    val finalString: String = newlinesRemove.mkString(" ")
    return finalString.trim
  }

  private def generateLineObject(traditional: Boolean,
                                 cedict: cedictMaps,
                                 frequency: frequencyMaps,
                                 storyInfo1of2: String,
                                 storyInfo2of2: String,
                                 lineAndLineInfoRaw: String) = {
    val lineInfo: String = lineAndLineInfoRaw.split("\n")(0).trim
    val lineContent: List[String] = lineAndLineInfoRaw.split("\n").drop(1).toList
    val newlinesRemoved: String = removeNewLineCharactersFromTest(lineContent)
    val cedictList: List[String] = getListOfWordsFromText(newlinesRemoved, traditional, cedict)
    val cedictObjects: List[cedictFreqObject] = cedictList.map(i =>
      getNaiveInfoFromWord(i, storyInfo1of2, storyInfo2of2, lineInfo, traditional, cedict, frequency))
    rawLineObject(storyInfo1of2, storyInfo2of2, lineInfo, newlinesRemoved, cedictObjects)
  }


}
