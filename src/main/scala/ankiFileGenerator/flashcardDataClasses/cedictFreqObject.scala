package ankiFileGenerator.flashcardDataClasses

case class cedictFreqObject(storyInfo1of2: String,
                            storyInfo2of2: String,
                            lineInfo: String,
                            traditionalHanzi: List[String],
                            simplifiedHanzi: List[String],
                            pinyin: List[String],
                            translation: List[String],
                            traditionalFrequency: List[Int],
                            simplifiedFrequency: List[Int])