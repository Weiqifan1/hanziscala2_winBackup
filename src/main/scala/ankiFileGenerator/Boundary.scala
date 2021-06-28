package ankiFileGenerator

import ankiFileGenerator.flashcardDataClasses.storyObject
import ankiFileGenerator.frequencyFileHandling.{generateTSVfile, objectSorting}
import ankiFileGenerator.frequencyFileHandling.loadFrequencyFiles.{readCedictMapsFromFile, readJundaAndTzaiMapsFromFile}

import java.io.{File, FileInputStream}
import scala.io.Source

object Boundary {

  /*val cedictTrad: Option[List[cedictObject]] = cedictMap.traditionalMap.get("我")
  println(cedictTrad)

  val frequency = readJundaAndTzaiMapsFromFile()
  val traditionalFrequency: List[String] = frequencyInfoTraditionalFromString("癮", frequency)
  val simplifiedFrequency: List[String] = frequencyInfoSimplifiedFromString("癮", frequency)
  println(traditionalFrequency)
  println(simplifiedFrequency)
*/
/*
  def parseTextFileAsStoryList(
                                storyFileContent: String,
                                traditional: Boolean,
                                cedict: cedictMaps,
                                frequency: frequencyMaps): storyObject = {*/



    def runAnkiFileGenerator(): Unit = {
        println("hej lykke")

        val fileContent: String = Source.fromFile("src/inputSystemFilesRaw/麻辣女孩01_01b.txt").mkString
        val cedictMap = readCedictMapsFromFile()
        val jundaAndTzai = readJundaAndTzaiMapsFromFile()

        val result: storyObject = generateTSVfile.parseTextFileAsStoryList(fileContent, true, cedictMap, jundaAndTzai)
        println(result.lineObjects.length)

        val firstFreq = result.lineObjects(0).cedictEntries(0)
        val secondFreq = result.lineObjects(0).cedictEntries(1)
        val thirdFreq = result.lineObjects(0).cedictEntries(2)


        val sortedLines =  objectSorting.sortLineObjectsByCharFrequency(result, true)


        println("end")
  }

}
