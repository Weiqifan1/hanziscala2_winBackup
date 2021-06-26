package inpuSystemLookup.dataClasses

import upickle.default.{ReadWriter => RW, macroRW}

case class textToMultipleCodesList(content: List[textToMultipleCodesObject])
/*
object textToMultipleCodesList{
  implicit val rw: RW[textToMultipleCodesList] = macroRW
}*/
