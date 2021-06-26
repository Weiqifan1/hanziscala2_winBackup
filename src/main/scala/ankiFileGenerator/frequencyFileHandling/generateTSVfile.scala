package ankiFileGenerator.frequencyFileHandling

import java.io.{File, PrintWriter}

object generateTSVfile {

  def writeTSVfile(textToWrite: String, title: String): Unit ={
    val writer = new PrintWriter(new File("outputFiles/" + title + ".tsv"))
    writer.write(textToWrite)
    writer.close()
  }



}
