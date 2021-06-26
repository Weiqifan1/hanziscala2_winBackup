package inpuSystemLookup.dataClasses

import upickle.default.{ReadWriter => RW, macroRW}

case class inputSystemCombinedMap(codeToInfo: inputSystemCodeToInfoMap, hanziToInfo: inputSystemHanziToInfoMap)
/*
object inputSystemCombinedMap{
  implicit val rw: RW[inputSystemCombinedMap] = macroRW
}*/
