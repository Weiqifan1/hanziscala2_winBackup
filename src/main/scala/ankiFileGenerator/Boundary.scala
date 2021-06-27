package ankiFileGenerator

import ankiFileGenerator.flashcardDataClasses.storyObject
import ankiFileGenerator.frequencyFileHandling.generateTSVfile.{parseTextFileAsStoryList, writeTSVfile}
import ankiFileGenerator.frequencyFileHandling.loadFrequencyFiles.{readCedictMapsFromFile, readJundaAndTzaiMapsFromFile}
import ankiFileGenerator.frequencyFileHandling.prepareTextHandlingMethods.getListOfWordsFromText
import inpuSystemLookup.dataClasses.{cedictMaps, frequencyMaps}

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

        val result: storyObject = parseTextFileAsStoryList(fileContent, true, cedictMap, jundaAndTzai)
        println(result.lineObjects.length)

    //val

    //val wordList = getListOfWordsFromText("如果123我說  衝,不停的麻煩大了",true, cedictMap)
    //val wordList = List("如果", "我", "說", "衝", "不停", "的", "麻煩", "大", "了");
    //println(wordList)
    //writeTSVfile("hej lykke", "third")



    println("end")
  }

}
