package inpuSystemLookup.dataClasses

//import upickle.default.{ReadWriter => RW, macroRW}

case class cedictMaps(traditionalMap: Map[String, List[cedictObject]], simplifiedMap: Map[String, List[cedictObject]])
  /*object cedictMaps{
    implicit val rw: RW[cedictMaps] = macroRW
  }*/

