package inpuSystemLookup.dataClasses

import upickle.default.{macroRW, ReadWriter => RW}

import scala.collection.mutable.ListBuffer

case class inputSystemHanziInfo(hanzi: String,
                                codes: List[String],
                                cedictSimp: Option[List[cedictObject]],
                                cedictTrad: Option[List[cedictObject]],
                                traditionalFrequency: List[String],
                                simplifiedFrequency: List[String]) extends Ordered [inputSystemHanziInfo] {
  def compare (that: inputSystemHanziInfo): Int = {
    val currentTradList: List[Int] = this.traditionalFrequency.map{case "0" => 100000; case x => x.toInt}.sorted.reverse
    val otherTradList: List[Int] = that.traditionalFrequency.map{case "0" => 100000; case x => x.toInt}.sorted.reverse
    val currentSimpList: List[Int] = this.simplifiedFrequency.map{case "0" => 100000; case x => x.toInt}.sorted.reverse
    val otherSimpList: List[Int] = that.simplifiedFrequency.map{case "0" => 100000; case x => x.toInt}.sorted.reverse
    val currentUnicode: List[Int] = this.hanzi.codePoints.toArray.toList.sorted
    val otherUnicode: List[Int] = that.hanzi.codePoints.toArray.toList.sorted

    val comparingTraditional = comparingFrequencyList(currentTradList, otherTradList)
    val comparingSimplified = comparingFrequencyList(currentSimpList, otherSimpList)
    val comparingUnicode = comparingFrequencyList(currentUnicode, otherUnicode)

    if (!comparingTraditional.equals(0)) {
      return comparingTraditional
    }else if (!comparingSimplified.equals(0)){
      return comparingSimplified
    }else {
      return comparingUnicode
    }
  }

  def comparingFrequencyList(currentTradList: List[Int], otherTradList: List[Int]): Int = {
    var comparisonResult: Int = 0
    val lengthOfShortestList: Int = List(currentTradList.length, otherTradList.length).min
    val rangeOfIndexes = 0 until lengthOfShortestList
    var integerBuffer: ListBuffer[Int] = new ListBuffer[Int]
    for (index <- rangeOfIndexes) {
      val currentFrequency = currentTradList(index)
      val otherFrequency = otherTradList(index)
      if (currentFrequency > otherFrequency) {
        integerBuffer.addOne(1)
      }else if (otherFrequency > currentFrequency){
        integerBuffer.addOne(-1)
      }
    }
    val finalList: List[Int] = integerBuffer.toList

    if (finalList.length == 0)
      comparisonResult = 0
    else if (finalList(0) == 1)
      comparisonResult = 1
    else
      comparisonResult = comparisonResult - 1;

    return comparisonResult
  }
}


/*
object inputSystemHanziInfo{
  implicit val rw: RW[inputSystemHanziInfo] = macroRW
}*/
