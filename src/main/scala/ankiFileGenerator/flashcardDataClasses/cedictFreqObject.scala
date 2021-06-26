package ankiFileGenerator.flashcardDataClasses

case class cedictFreqObject(traditionalHanzi: List[String],
                            simplifiedHanzi: List[String],
                            pinyin: List[String],
                            translation: List[String],
                            traditionalFrequency: List[Int],
                            simplifiedFrequency: List[Int])