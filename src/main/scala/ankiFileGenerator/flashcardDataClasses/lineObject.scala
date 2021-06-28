package ankiFileGenerator.flashcardDataClasses

case class lineObject(storyInfo1of2: String,
                      storyInfo2of2: String,
                      lineInfo: String,
                      originallLine: String,
                      cedictEntries: List[cedictFreqObject])
