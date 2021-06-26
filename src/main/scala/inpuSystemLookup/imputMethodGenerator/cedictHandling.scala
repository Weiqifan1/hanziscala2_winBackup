package inpuSystemLookup.imputMethodGenerator

import inpuSystemLookup.dataClasses.{cedictMaps, cedictObject, cedictTempTuple}

import java.io.{FileInputStream, FileOutputStream, ObjectInputStream, ObjectOutputStream}
import scala.collection.mutable
import scala.collection.mutable.ListBuffer

//cedictObject(traditionalHanzi: String, simplifiedHanzi: String, pinyin: String, translation: String)
object cedictHandling {

  def listBufferContainsCedictObject(cedictObj: cedictObject, buffer: ListBuffer[cedictObject]): Boolean ={
    var result = false
    for (each: cedictObject <- buffer.toList) {
      val obj1 = cedictObj.traditionalHanzi
      val each1 = each.traditionalHanzi
      val first = obj1.equals(each1)

      val obj2 = cedictObj.simplifiedHanzi
      val each2 = each.simplifiedHanzi
      val second = obj2.equals(each2)

      val obj3 = cedictObj.pinyin
      val each3 = each.pinyin
      val third = obj3.equals(each3)

      val obj4 = cedictObj.translation
      val each4 = each.translation
      val forth = obj4.equals(each4)

      if (first && second && third && forth){
        result = true
      }
    }
    return result
  }

  def lastHanziGroupInLoop(finalChar: String, eachObj: cedictObject, traditional: Boolean): Boolean = {
    var result: Boolean = if (traditional) {finalChar.equals(eachObj.traditionalHanzi)} else {finalChar.equals(eachObj.simplifiedHanzi)}
    return result
  }

  def cedictObjMatchesCurrentChar(each: cedictObject, currentChar: String, traditional: Boolean): Boolean = {
    val objectHanzi = if (traditional) {each.traditionalHanzi} else {each.simplifiedHanzi}
    if (objectHanzi.equals(currentChar)) {return true} else {return false}
  }

  def generateNestedCedictListTradOrSimp(objectList: List[cedictObject], traditional: Boolean): List[cedictTempTuple] = {
    var protoSimpMap: ListBuffer[cedictTempTuple] = new ListBuffer[cedictTempTuple]
    var tempSimp: ListBuffer[cedictObject] = new ListBuffer[cedictObject]

    val simpSortedObjects: List[cedictObject] = if (traditional) {objectList.sortBy(_.traditionalHanzi)} else {objectList.sortBy(_.simplifiedHanzi)}
    var currentChars: String = if (traditional) { simpSortedObjects(0).traditionalHanzi} else {simpSortedObjects(0).simplifiedHanzi}

    val finalChar: String = if (traditional) {simpSortedObjects(simpSortedObjects.length - 1).traditionalHanzi} else {simpSortedObjects(simpSortedObjects.length - 1).simplifiedHanzi}

    val finalCharList: ListBuffer[cedictObject] = new ListBuffer[cedictObject]
    for (eachSimp <- simpSortedObjects) {
      if(traditional && eachSimp.traditionalHanzi.equals("誒")){
        val temp = ""
      }
      if (lastHanziGroupInLoop(finalChar, eachSimp, traditional)) {
        finalCharList += eachSimp
      } else {
        if (cedictObjMatchesCurrentChar(eachSimp, currentChars, traditional) && !listBufferContainsCedictObject(eachSimp, tempSimp)) {
          tempSimp += eachSimp
        } else {
          protoSimpMap += cedictTempTuple((currentChars, tempSimp.toSet.toList))
          currentChars = if (traditional) {eachSimp.traditionalHanzi} else {eachSimp.simplifiedHanzi}
          tempSimp = new ListBuffer[cedictObject]
          tempSimp += eachSimp
        }
      }
    }
    protoSimpMap += cedictTempTuple((finalChar, finalCharList.toList))
    return protoSimpMap.toList
  }

  def createCedictMap(objectList: List[cedictObject]): cedictMaps ={
    val finalSimp: List[cedictTempTuple] = generateNestedCedictListTradOrSimp(objectList, false)
    val finalTrad: List[cedictTempTuple] = generateNestedCedictListTradOrSimp(objectList, true)

    //println("finalTraddoubleListlength")
    //val printOutTrad = finalTrad.filter(each => each.tuple._2.length > 1)
    //println(printOutTrad.length)
    //println("finalSimpdoubleListlength")
    //val printoutSimp = finalSimp.filter(each => each.tuple._2.length > 1)
    //println(printoutSimp.length)

    val finalTradMap2: Map[String, List[cedictObject]] = finalTrad.map(tup => tup.tuple._1 -> tup.tuple._2).toMap
    val finalSimpMap2: Map[String, List[cedictObject]] = finalSimp.map(tup => tup.tuple._1 -> tup.tuple._2).toMap
    return cedictMaps(finalTradMap2, finalSimpMap2)
  }

  def getAllTradDubletWordsFromCedict(objectList: List[cedictObject]): Set[String] ={
    val tradSortedObjects: List[cedictObject] = objectList.sortBy(_.traditionalHanzi)

    var traditionalOldList: mutable.SortedSet[String] = scala.collection.mutable.SortedSet[String]()
    var traditionalList: mutable.SortedSet[String] = scala.collection.mutable.SortedSet[String]()

    for (each <- tradSortedObjects) {
      if (traditionalOldList.contains(each.traditionalHanzi)) {
        traditionalList += each.traditionalHanzi
      }
      traditionalOldList += each.traditionalHanzi
    }

    val result = traditionalList.toSet
    return result
  }
  def getAllSimpDubletWordsFromCedict(objectList: List[cedictObject]): Set[String] ={
    val simpSortedObjects: List[cedictObject] = objectList.sortBy(_.simplifiedHanzi)

    var simplifiedOldList: mutable.SortedSet[String] = scala.collection.mutable.SortedSet[String]()
    var simplifiedList: mutable.SortedSet[String] = scala.collection.mutable.SortedSet[String]()

    for (each <- simpSortedObjects) {
      if (simplifiedOldList.contains(each.simplifiedHanzi)) {
        simplifiedList += each.simplifiedHanzi
      }
      simplifiedOldList += each.simplifiedHanzi
    }

    val result = simplifiedList.toSet
    return result
  }

  def getCedictObjectList(): List[cedictObject] = {
    val filePath = "src/main/resources/frequencyfilesRaw/cedict_ts.txt"
    val hanzilines: List[String] = scala.io.Source.fromFile(filePath).mkString.split("\n").toList
    val splitlines: List[Array[String]] = hanzilines map {line => line.split("(\\s+\\[)|(\\]\\s+)")}
    val objectList: List[cedictObject] = splitlines.map(each => cedictObject(each(0).toString.split("\\s")(0),
        each(0).toString.split("\\s")(1),
        each(1).toString,
        each(2).toString))
    return objectList
  }

  def getCedict(): cedictMaps = {
    val objectList = getCedictObjectList()
    val getFinalMap = createCedictMap(objectList)
    return getFinalMap
  }
/*
  def getCedictHanziToTranslationMap(): cedictMaps ={
    //val getcedictObjecyList = getCedictObjectList()
    //val finalCedictMaps: cedictMaps = createCedictMap(getcedictObjecyList)
    //val oos = new ObjectOutputStream(new FileOutputStream("src/main/resources/serializedDataFiles/cedictMaps.txt"))
    //oos.writeObject(finalCedictMaps)
    //oos.close
    //println("serialized cedict saved to file")

    val ois = new ObjectInputStream(new FileInputStream("src/main/resources/serializedDataFiles/cedictMaps.txt"))
    val readMapsIn = ois.readObject.asInstanceOf[cedictMaps]
    ois.close
    return readMapsIn
    /*
    println("serialized cedict readIn")

    val tradset = getAllTradDubletWordsFromCedict(getcedictObjecyList)
    val simpset = getAllSimpDubletWordsFromCedict(getcedictObjecyList)
    println("dubletTradWords")
    println(tradset.toSet.size)
    println("dubletSimplifiedWords")
    println(simpset.toSet.size)
    val firstTrad = tradset.toList(0)
    val fristsimp = simpset.toList(0)

    println(readMapsIn.traditionalMap(firstTrad))
    println(readMapsIn.simplifiedMap(fristsimp))
    println("success")

    return null//cedictMaps(traditionalHanzi, simplifiedHanzi)*/
  }*/
}

