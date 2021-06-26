package ankiFileGenerator

import ankiFileGenerator.frequencyFileHandling.generateTSVfile.writeTSVfile
import ankiFileGenerator.frequencyFileHandling.loadFrequencyFiles.{readCedictMapsFromFile, readJundaAndTzaiMapsFromFile}
import ankiFileGenerator.frequencyFileHandling.prepareTextHandlingMethods.getListOfWordsFromText

object Boundary {

  /*val cedictTrad: Option[List[cedictObject]] = cedictMap.traditionalMap.get("我")
  println(cedictTrad)

  val frequency = readJundaAndTzaiMapsFromFile()
  val traditionalFrequency: List[String] = frequencyInfoTraditionalFromString("癮", frequency)
  val simplifiedFrequency: List[String] = frequencyInfoSimplifiedFromString("癮", frequency)
  println(traditionalFrequency)
  println(simplifiedFrequency)
*/

  def runAnkiFileGenerator(): Unit = {
    println("hej lykke")

    //val cedictMap = readCedictMapsFromFile()
    //val wordList = getListOfWordsFromText("如果123我說  衝,不停的麻煩大了",true, cedictMap)

    //writeTSVfile("hej lykke", "third")

    println("end")
  }

}
