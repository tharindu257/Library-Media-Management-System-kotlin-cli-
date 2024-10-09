package media_management.ebookformat

import media_management.Media

class EBook(
    title: String,
    author: String,
    publicationYear: Int,
    val fileSize: Double,
    val format: EBookFormat
) : Media(title, author, publicationYear) {
    override fun getDetails() = "EBook: $title by $author, $publicationYear, Format: $format, File size: $fileSize MB"
}