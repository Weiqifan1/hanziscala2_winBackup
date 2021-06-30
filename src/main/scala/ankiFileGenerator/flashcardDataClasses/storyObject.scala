package ankiFileGenerator.flashcardDataClasses

case class storyObject(traditional: Boolean,
                       storyInfo1of2: String,
                       storyInfo2of2: String,
                       originalUnsortedLines: List[rawLineObject],
                       cedictEntriesIgnored: List[String],
                       flashcardTotalCedictEntriesUnsorted: List[String],
                       flashcardTotalCharacters: List[String],
                       flashcardlineObjects: List[flashcardLineObject])
