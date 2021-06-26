package inpuSystemLookup.testPreparation

object hashmapTestPrepare {

  //val simple: List[String] = listOf5000Simplified()
  //    val traditional: List[String] = listOf5000Traditional()
  //    println(traditional(0))
  //    println(traditional(1))
  //    println(traditional.length)

  //create two lists of characters for use with testing hashmap
  def listOf5000Simplified(): List[String] = {
    val lines: String = scala.io.Source.fromFile("src/main/resources/frequencyfilesRaw/testJunda.txt").mkString
    val output = lines.split("").toList
    return output
  }

  def listOf5000Traditional(): List[String] = {
    val lines: String = scala.io.Source.fromFile("src/main/resources/frequencyfilesRaw/testTzai.txt").mkString
    val output = lines.split("").toList
    return output
  }



}
