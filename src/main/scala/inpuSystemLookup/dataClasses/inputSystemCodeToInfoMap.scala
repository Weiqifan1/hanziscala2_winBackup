package inpuSystemLookup.dataClasses

import upickle.default.{ReadWriter => RW, macroRW}

case class inputSystemCodeToInfoMap(content: Map[String, inputSystemHanziInfoList])
/*
object inputSystemCodeToInfoMap{
  implicit val rw: RW[inputSystemCodeToInfoMap] = macroRW
}*/
