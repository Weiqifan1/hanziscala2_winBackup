package inpuSystemLookup.dataClasses

//import upickle.default.{ReadWriter => RW, macroRW}

case class inputSystemHanziToInfoMap(content: Map[String, inputSystemHanziInfoList])
/*
object inputSystemHanziToInfoMap{
  implicit val rw: RW[inputSystemHanziToInfoMap] = macroRW
}*/
