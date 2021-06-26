package inpuSystemLookup.dataClasses

import upickle.default.{ReadWriter => RW, macroRW}

case class textToMultipleCodesObject(hanzi: String, codeList: List[String])
/*
object textToMultipleCodesObject{
  implicit val rw: RW[textToMultipleCodesObject] = macroRW
}*/
