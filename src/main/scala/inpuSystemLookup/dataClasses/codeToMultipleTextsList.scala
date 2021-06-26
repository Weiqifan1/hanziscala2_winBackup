package inpuSystemLookup.dataClasses

import upickle.default.{ReadWriter => RW, macroRW}

case class codeToMultipleTextsList(content: List[codeToMultipleTextsObject])
/*
object codeToMultipleTextsList{
  implicit val rw: RW[codeToMultipleTextsList] = macroRW
}*/
