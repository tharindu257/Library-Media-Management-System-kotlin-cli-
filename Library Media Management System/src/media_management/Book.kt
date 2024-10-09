package media_management

class Book(
    title: String,
    author: String,
    publicationYear: Int,
    val numberOfPages: Int
) : Media(title, author, publicationYear) {
    override fun getDetails() = "Book: $title by $author, $publicationYear, Pages: $numberOfPages"
}