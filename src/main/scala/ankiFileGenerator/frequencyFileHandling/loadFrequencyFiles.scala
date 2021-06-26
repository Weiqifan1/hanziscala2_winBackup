package ankiFileGenerator.frequencyFileHandling

import inpuSystemLookup.dataClasses.{cedictMaps, frequencyMaps}

import java.io.{File, FileInputStream, ObjectInputStream}

object loadFrequencyFiles {
  def readCedictMapsFromFile(): cedictMaps = {
    val inputFile = new File("src/main/resources/frequencyFilesSerialized/cedictSerialized_cedictMaps.txt")
    val fileInputStream = new FileInputStream(inputFile)
    val objectInputStream = new ObjectInputStream(fileInputStream)
    val outputCedictMaps = objectInputStream.readObject().asInstanceOf[cedictMaps]
    return outputCedictMaps
  }

  def readJundaAndTzaiMapsFromFile(): frequencyMaps = {
    val inputFile = new File("src/main/resources/frequencyFilesSerialized/jundaAndTzaiSerialized_frequencyMaps.txt")
    val fileInputStream = new FileInputStream(inputFile)
    val objectInputStream = new ObjectInputStream(fileInputStream)
    val outputfrequencyMaps = objectInputStream.readObject().asInstanceOf[frequencyMaps]
    return outputfrequencyMaps
  }
}
