package ankiFileGenerator.frequencyFileHandling

import ankiFileGenerator.flashcardDataClasses.{cedictFreqObject, rawLineObject, storyObject}

import scala.collection.mutable.ListBuffer

object objectSorting {

  def removeRedundantLines(sObj: List[rawLineObject], traditional: Boolean): List[rawLineObject] = {
    //val sortedLines: List[rawLineObject] = sObj.lineObjects
    var cumulativeCedict = new ListBuffer[String]()
    var newListOfLines = new ListBuffer[rawLineObject]()
    for (each: rawLineObject <- sObj) {
      val lineObjectCedict: List[String] = getAllcedictEntriesFromLine(each, traditional)
      var missingCedict: Boolean = containMissingCedict(lineObjectCedict, cumulativeCedict)
      if (missingCedict){
        newListOfLines.addOne(each)
        cumulativeCedict.addAll(lineObjectCedict)
      }
    }
    return newListOfLines.toList //new storyObject(
      //sObj.storyInfo1of2, sObj.storyInfo2of2, newListOfLines.toList)
  }

  private def containMissingCedict(cedictEntriesInLine: List[String], cumulativeEntries: ListBuffer[String]): Boolean = {
    var someEntriesInLineAreMissingFromCumulativeList: Boolean = false
    for (eachEntry: String <- cedictEntriesInLine) {
      if (!cumulativeEntries.contains(eachEntry)){
        someEntriesInLineAreMissingFromCumulativeList = true
      }
    }
    val result: Boolean = someEntriesInLineAreMissingFromCumulativeList
    return result
  }

  private def getAllcedictEntriesFromLine(line: rawLineObject, traditional: Boolean): List[String] = {
    val freqNumbers: List[String] = {
      if (traditional){
        line.cedictEntries.map(i => i.traditionalHanzi).flatten
      }else {
        line.cedictEntries.map(i => i.simplifiedHanzi).flatten
      }
    }
    val uniqueValues: List[String] =
      freqNumbers.toSet.toList.sorted
    return uniqueValues
  }

  private def getAllFrequencyNumbersFromLine(line: rawLineObject, traditional: Boolean): List[Int] = {
    val freqNumbers: List[Int] = {
      if (traditional){
        line.cedictEntries.map(i => i.traditionalFrequency).flatten
      }else {
        line.cedictEntries.map(i => i.simplifiedFrequency).flatten
      }
    }
    val uniqueNumbers: List[Int] =
      freqNumbers.toSet.toList.sorted
    return uniqueNumbers
  }

  def sortLineObjectsByCharFrequency(rawLineObjects: List[rawLineObject], traditional: Boolean): List[rawLineObject] = {
    //val lineObjects: List[rawLineObject] = story.lineObjects
    val lineTupples: List[(Int, rawLineObject)] =
      rawLineObjects.map(i => {(getHighestFrequencyInLineObject(i, traditional),i)})
    val sortedTupples: List[(Int, rawLineObject)] = lineTupples.sortWith(_._1 < _._1)
    val sortedLines: List[rawLineObject] = sortedTupples.map(i => i._2)
    //val newStory: storyObject =
    //  storyObject(story.storyInfo1of2, story.storyInfo2of2, sortedLines)
    return sortedLines
  }

  /*users.sortWith(_.age > _.age) shouldBe List(
  User("Mike", 43),
  User("Kelly", 21),
  User("Mike", 16)
)*/

  private def getHighestFrequencyInLineObject(lineObj: rawLineObject, traditional: Boolean): Int = {
    val getCedictObjects: List[cedictFreqObject] = lineObj.cedictEntries
    val listOfAllFreqObjects: List[Int] =
      if (traditional) {
        getCedictObjects.map(i => i.traditionalFrequency).flatten
      }else {
        getCedictObjects.map(i => i.simplifiedFrequency).flatten
      }
    val sortedFreq = listOfAllFreqObjects.sorted
    val highestFreq = if (sortedFreq.isEmpty) 0 else sortedFreq.last
    return highestFreq
  }

  private def getLowestFrequencyInCedictObject(cedictObj: cedictFreqObject, traditional: Boolean): Int = {
    val frequencies: List[Int] = if (traditional){cedictObj.traditionalFrequency}else{cedictObj.simplifiedFrequency}
    val sortedFrequencies: List[Int] = frequencies.sorted
    return sortedFrequencies(0)
  }

}
