package media_management

class DigitalMagazine(
    title: String,
    author: String,
    publicationYear: Int,
    val fileSize: Double,
    val countryOfIssue: String
) : Media(title, author, publicationYear) {
    override fun getDetails() = "Digital Magazine: $title by $author, $publicationYear, Country: $countryOfIssue, File size: $fileSize MB"
}