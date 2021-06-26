package inpuSystemLookup.dataClasses

import upickle.default.{ReadWriter => RW, macroRW}

case class cedictObject(traditionalHanzi: String, simplifiedHanzi: String, pinyin: String, translation: String)
/*
object cedictObject{
  implicit val rw: RW[cedictObject] = macroRW
}*/
