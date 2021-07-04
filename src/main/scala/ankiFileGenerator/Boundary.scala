package ankiFileGenerator

import ankiFileGenerator.flashcardDataClasses.{flashcardLineObject, rawLineObject, storyObject}
import ankiFileGenerator.frequencyFileHandling.generateStoryObject.{createStoryObjectFromFile, parseTextFileAsRawLineList}
import ankiFileGenerator.generateTSVsforAnki.generateTSVfile.writeTSVfile
import ankiFileGenerator.frequencyFileHandling.objectSorting
import ankiFileGenerator.frequencyFileHandling.loadFrequencyFiles.{readCedictMapsFromFile, readJundaAndTzaiMapsFromFile}
import ankiFileGenerator.generateTSVsforAnki.testTSV.{writeTSVfileTEST}

import java.io.{File, FileInputStream}
import scala.io.Source
//https://www.thinkific.com/blog/best-text-to-speech-apps-and-software/
//https://cloud.google.com/text-to-speech/docs/quickstart-client-libraries
object Boundary {

    //private def createStoryObject()

    def runAnkiFileGenerator(): Unit = {
        println("hej lykke")

        val fileContentRAW: String = Source.fromFile("src/inputSystemFilesRaw/麻辣女孩01_01b.txt").mkString
        val fileContent: String = fileContentRAW.replace(';', ':').replace('\r', ' ')
        val cedictMap = readCedictMapsFromFile()
        val jundaAndTzai = readJundaAndTzaiMapsFromFile()

        val result: List[rawLineObject] = parseTextFileAsRawLineList(fileContent, true, cedictMap, jundaAndTzai)
        println(result.length)

        val firstFreq = result(0).cedictEntries(0)
        val secondFreq = result(0).cedictEntries(1)
        val thirdFreq = result(0).cedictEntries(2)


        val sortedLines: List[rawLineObject] =
            objectSorting.sortLineObjectsByCharFrequency(result, true)
        //val filteredLines = objectSorting.removeRedundantLines(sortedLines, true)

        val filtered: List[flashcardLineObject] = objectSorting.generateFlashCardObjectsNoAudio(sortedLines, result, List(), true)

        /*createStoryObjectFromFile(filePath: String,
                                cedictEntriesToIgnore: List[String],
                                traditional: Boolean,
                                cedictMapTradAndSimp: cedictMaps,
                                freqMapsTzaiAndJunda: frequencyMaps): storyObject = {*/

        //create a new object that can hold lines as well as info needed for anki flashcards
        val finalStory: storyObject = createStoryObjectFromFile("src/inputSystemFilesRaw/麻辣女孩01_01b.txt", List(), true, cedictMap, jundaAndTzai)

        println("******* create TSV file ********")

        writeTSVfileTEST(finalStory, "testTSV_File")

        val maResult = cedictMap.traditionalMap.get("嗎")

        println("end")
  }

}
