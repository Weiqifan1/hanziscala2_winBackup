package inpuSystemLookup.imputMethodGenerator

import inpuSystemLookup.dataClasses.frequencyMaps

import scala.collection.mutable.ListBuffer
object jundaAndTzaiHandling {

  def getJundaAndTzaiMaps(): frequencyMaps ={
    val tzaiMap = getTzaiCharToNumMap()
    val jundaMap = getJundaCharToNumMap()
    val maps = frequencyMaps(tzaiMap, jundaMap)
    return maps
  }

  def removeInitialByteOrderMarkEct(input: String): String ={
    //Zero Width No-Break Space: 65279
    val badCharacters: List[Int] = List(65279)
    val firstCharacter: Int = input.chars().findFirst().getAsInt
    var inputWithoutBom: String = ""
    if (!badCharacters.contains(firstCharacter)) {
      inputWithoutBom = input
    }else {
      inputWithoutBom = input.substring(1)
    }
    return inputWithoutBom
  }

  def getJundaCharToNumMap(): Map[String, String] ={
    val filePath = "src/main/resources/frequencyfilesRaw/Junda2005.txt"
    val rawInputFromFile: String = scala.io.Source.fromFile(filePath).mkString
    val inputWithoutBom: String = removeInitialByteOrderMarkEct(rawInputFromFile)

    val hanzilines: List[String] = inputWithoutBom.split("\n").toList

    //foerste er en integer og anden er et tegn
    val splitlines = hanzilines map {line => line.split("\\s+").slice(0,2)}

    val jundaHashmap = splitlines.map(i => i(1).trim -> i(0).trim).toMap
    return jundaHashmap
  }

  def getTzaiCharToNumMap(): Map[String, String] ={
    val filePath = "src/main/resources/frequencyfilesRaw/Tzai2006.txt"

    val rawInputFromFile: String = scala.io.Source.fromFile(filePath).mkString
    val inputWithoutBom: String = removeInitialByteOrderMarkEct(rawInputFromFile)
    val hanzilines: List[String] = inputWithoutBom.split("\n").toList

    //foerste er tegn andet er totale antal forekomster
    var i = 0
    val splitlines = hanzilines map {line => line.split("\\s+").slice(0,2)}

    var updatedSplitLines = ListBuffer[List[String]]()
    for (eachLine <- splitlines){
      i += 1
      updatedSplitLines += List(eachLine(0), i.toString)
    }
    val finalList = updatedSplitLines.toList
    val jundaHashmap = finalList.map(i => i(0).trim -> i(1).trim).toMap

    return jundaHashmap
  }
}
