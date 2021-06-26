package inpuSystemLookup.dataClasses

import upickle.default.{ReadWriter => RW, macroRW}

case class inputSystemTemp(codeToList: List[(String, codeToTextList)], hanziToList: List[(String, codeToTextList)])
/*
object inputSystemTemp{
  implicit val rw: RW[inputSystemTemp] = macroRW
}*/
