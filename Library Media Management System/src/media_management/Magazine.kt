package media_management

class Magazine(
    title: String,
    author: String,
    publicationYear: Int,
    val numberOfPages: Int,
    val countryOfIssue: String
) : Media(title, author, publicationYear) {
    override fun getDetails() = "Magazine: $title by $author, $publicationYear, Country: $countryOfIssue, Pages: $numberOfPages"
}