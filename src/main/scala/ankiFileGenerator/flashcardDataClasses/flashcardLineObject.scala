package ankiFileGenerator.flashcardDataClasses

case class flashcardLineObject(lineObj: rawLineObject,
                               newCedictEntries: List[cedictFreqObject],
                               lineAudio: List[String],
                               previousLineObj: rawLineObject,
                               nextLineObject: rawLineObject)
