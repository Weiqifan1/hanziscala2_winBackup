package inpuSystemLookup.dataClasses

import upickle.default.{ReadWriter => RW, macroRW}

case class codeToMultipleTextsObject(code: String, hanziList: List[String])
/*
object codeToMultipleTextsObject{
  implicit val rw: RW[codeToMultipleTextsObject] = macroRW
}*/
