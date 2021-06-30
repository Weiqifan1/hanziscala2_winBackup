package ankiFileGenerator.flashcardDataClasses

case class storyObject(storyInfo1of2: String,
                       storyInfo2of2: String,
                       originalUnsortedLines: List[rawLineObject],
                       cedictEntriesIgnored: List[String],
                       flashcardCedictEntriesUnsorted: List[String],
                       flashcardlineObjects: List[flashcardLineObject])
