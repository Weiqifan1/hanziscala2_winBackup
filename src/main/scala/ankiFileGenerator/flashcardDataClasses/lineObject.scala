package ankiFileGenerator.flashcardDataClasses

case class lineObject(storyInfo1of2: String,
                      storyInfo2of2: String,
                      lineInfo: String,
                      cedictEntries: List[cedictFreqObject])
