package media_management

abstract class Media(
    val title: String,
    val author: String,
    val publicationYear: Int,
    var isAvailable: Boolean = true,
    var returnDate: String? = null
) {
    abstract fun getDetails(): String
}