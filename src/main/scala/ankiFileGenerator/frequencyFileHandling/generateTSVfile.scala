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



}
