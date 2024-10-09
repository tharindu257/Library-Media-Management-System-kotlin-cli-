package media_management

class Audiobook(
    title: String,
    author: String,
    publicationYear: Int,
    val fileSize: Double,
    val duration: Int,
    val narrator: String
) : Media(title, author, publicationYear) {
    override fun getDetails() = "Audiobook: $title by $author, $publicationYear, Narrator: $narrator, Duration: $duration mins, File size: $fileSize MB"
}