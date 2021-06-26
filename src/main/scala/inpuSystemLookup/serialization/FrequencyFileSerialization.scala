package inpuSystemLookup.serialization

import inpuSystemLookup.dataClasses.{cedictMaps, frequencyMaps}
import inpuSystemLookup.imputMethodGenerator.cedictHandling.getCedict
import inpuSystemLookup.imputMethodGenerator.jundaAndTzaiHandling.getJundaAndTzaiMaps

import java.io.{File, FileInputStream, FileOutputStream, ObjectInputStream, ObjectOutputStream}

object FrequencyFileSerialization {

  def serializeCedictAndFrequencyFiles(): Unit ={
    serializeJundaAndTzai()
    serializeCedict()
  }

  def serializeJundaAndTzai(): Unit = {
    val frequencyMap = getJundaAndTzaiMaps()
    val outputFile = new File("src/main/resources/frequencyFilesSerialized/jundaAndTzaiSerialized_frequencyMaps.txt")
    val fileOutputStream = new FileOutputStream(outputFile)
    val objectOutputStream = new ObjectOutputStream(fileOutputStream)
    objectOutputStream.writeObject(frequencyMap)
    objectOutputStream.close()
  }

  def readJundaAndTzaiMapsFromFile(): frequencyMaps = {
    val inputFile = new File("src/main/resources/frequencyFilesSerialized/jundaAndTzaiSerialized_frequencyMaps.txt")
    val fileInputStream = new FileInputStream(inputFile)
    val objectInputStream = new ObjectInputStream(fileInputStream)
    val outputfrequencyMaps = objectInputStream.readObject().asInstanceOf[frequencyMaps]
    return outputfrequencyMaps
  }

  def serializeCedict(): Unit = {
    val cedictObj: cedictMaps = getCedict()
    //val filePath = "src/main/resources/frequencyfilesRaw/cedict_ts.txt"
    val outputFile = new File("src/main/resources/frequencyFilesSerialized/cedictSerialized_cedictMaps.txt")
    val fileOutputStream = new FileOutputStream(outputFile)
    val objectOutputStream = new ObjectOutputStream(fileOutputStream)
    objectOutputStream.writeObject(cedictObj)
    objectOutputStream.close()
  }

  def readCedictMapsFromFile(): cedictMaps = {
    val inputFile = new File("src/main/resources/frequencyFilesSerialized/cedictSerialized_cedictMaps.txt")
    val fileInputStream = new FileInputStream(inputFile)
    val objectInputStream = new ObjectInputStream(fileInputStream)
    val outputCedictMaps = objectInputStream.readObject().asInstanceOf[cedictMaps]
    return outputCedictMaps
  }


}
