package ankiFileGenerator.frequencyFileHandling

import ankiFileGenerator.flashcardDataClasses.{cedictFreqObject, lineObject, storyObject}

object objectSorting {

  def sortLineObjectsByCharFrequency(story: storyObject, traditional: Boolean): storyObject = {
    val lineObjects: List[lineObject] = story.lineObjects
    val lineTupples: List[(Int, lineObject)] =
      lineObjects.map(i => {(getHighestFrequencyInLineObject(i, traditional),i)})
    val sortedTupples: List[(Int, lineObject)] = lineTupples.sortWith(_._1 < _._1)
    val sortedLines: List[lineObject] = sortedTupples.map(i => i._2)
    val newStory: storyObject =
      storyObject(story.storyInfo1of2, story.storyInfo2of2, sortedLines)
    return newStory
  }

  /*users.sortWith(_.age > _.age) shouldBe List(
  User("Mike", 43),
  User("Kelly", 21),
  User("Mike", 16)
)*/

  private def getHighestFrequencyInLineObject(lineObj: lineObject, traditional: Boolean): Int = {
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
