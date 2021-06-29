package ankiFileGenerator

import ankiFileGenerator.flashcardDataClasses.{flashcardLineObject, rawLineObject, storyObject}
import ankiFileGenerator.frequencyFileHandling.{generateTSVfile, objectSorting}
import ankiFileGenerator.frequencyFileHandling.loadFrequencyFiles.{readCedictMapsFromFile, readJundaAndTzaiMapsFromFile}

import java.io.{File, FileInputStream}
import scala.io.Source
//https://www.thinkific.com/blog/best-text-to-speech-apps-and-software/
//https://cloud.google.com/text-to-speech/docs/quickstart-client-libraries
object Boundary {

    def runAnkiFileGenerator(): Unit = {
        println("hej lykke")

        val fileContent: String = Source.fromFile("src/inputSystemFilesRaw/麻辣女孩01_01b.txt").mkString
        val cedictMap = readCedictMapsFromFile()
        val jundaAndTzai = readJundaAndTzaiMapsFromFile()

        val result: List[rawLineObject] = generateTSVfile.parseTextFileAsRawLineList(fileContent, true, cedictMap, jundaAndTzai)
        println(result.length)

        val firstFreq = result(0).cedictEntries(0)
        val secondFreq = result(0).cedictEntries(1)
        val thirdFreq = result(0).cedictEntries(2)


        val sortedLines: List[rawLineObject] =
            objectSorting.sortLineObjectsByCharFrequency(result, true)
        //val filteredLines = objectSorting.removeRedundantLines(sortedLines, true)

        val filtered: List[flashcardLineObject] = objectSorting.generateFlashCardObjectsNoAudio(sortedLines, true)

        //create a new object that can hold lines as well as info needed for anki flashcards


        println("end")
  }

}
