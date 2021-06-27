package ankiFileGenerator.frequencyFileHandling

import ankiFileGenerator.flashcardDataClasses.{cedictFreqObject, lineObject, storyObject}
import ankiFileGenerator.frequencyFileHandling.prepareTextHandlingMethods.{getListOfWordsFromText, getNaiveInfoFromWord}
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
  def parseTextFileAsStoryList(
                               storyFileContent: String,
                               traditional: Boolean,
                               cedict: cedictMaps,
                               frequency: frequencyMaps): storyObject = {
    val regexToUse: String = "\n[\\s]*\n"
    val parsingResult: List[String] = storyFileContent.split(regexToUse).toList
    val storyInfoRaw: List[String] = parsingResult(0).split("\n").toList
    val storyInfo1of2: String = storyInfoRaw(0)
    val storyInfo2of2: String = if (storyInfoRaw.length > 1){storyInfoRaw(1)}else{""}
    val lines: List[lineObject] = parsingResult.drop(1).map(i =>{
      generateLineObject(traditional, cedict, frequency, storyInfo1of2, storyInfo2of2, i)
    })
    val resultStoryObject: storyObject =
      storyObject(storyInfo1of2, storyInfo2of2, lines)
    return resultStoryObject
  }

  private def generateLineObject(traditional: Boolean,
                                 cedict: cedictMaps,
                                 frequency: frequencyMaps,
                                 storyInfo1of2: String,
                                 storyInfo2of2: String,
                                 i: String) = {
    val lineInfo: String = i.split("\n")(0)
    val lineContent: String = i.split("\n")(1)
    val cedictList: List[String] = getListOfWordsFromText(lineContent, traditional, cedict)
    val cedictObjects: List[cedictFreqObject] = cedictList.map(i =>
      getNaiveInfoFromWord(i, storyInfo1of2, storyInfo2of2, lineInfo, traditional, cedict, frequency))
    lineObject(storyInfo1of2, storyInfo2of2, lineInfo, cedictObjects)
  }
}
