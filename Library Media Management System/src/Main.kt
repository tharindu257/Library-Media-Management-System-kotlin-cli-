import catalog.Catalog
import media_management.*
import media_management.ebookformat.*
import users.*
import kotlin.system.exitProcess

fun main() {
    val catalog = Catalog()
    val staff = StaffUser("A001", "Hirumitha")

    println("\nWelcome to the New York Public Library Media Management System")
    println("--------------------------------------------------------------")

    while (true) {
        println("\n1. Add Media")
        println("2. Remove Media")
        println("3. Search Media")
        println("4. Borrow Media")
        println("5. Return Media")
        println("6. Get Recommendations")
        println("7. View Media Count")
        println("8. Edit Media")
        println("9. Exit")
        print("\nChoose an option: ")

        when (readLine()) {
            "1" -> addMedia(catalog, staff)
            "2" -> removeMedia(catalog, staff)
            "3" -> searchMedia(catalog)
            "4" -> borrowMedia(catalog)
            "5" -> returnMedia(catalog)
            "6" -> getRecommendations(catalog)
            "7" -> viewMediaCount(catalog, staff)
            "8" -> editMediaPrompt(catalog, staff)
            "9" -> {
                println("Thank you for using the system. Goodbye!")
                exitProcess(0)
            }
            else -> println("Invalid option. Please try again.")
        }
    }
}

fun addMedia(catalog: Catalog, staff: StaffUser) {
    println("Enter the type of media (Book, Magazine, EBook, Audiobook, DigitalMagazine):")
    when (readLine() ?: "") {
        "Book" -> {
            val book = Book(
                title = prompt("Enter title:"),
                author = prompt("Enter author:"),
                publicationYear = prompt("Enter publication year:").toIntOrNull() ?: return invalidInput(),
                numberOfPages = prompt("Enter number of pages:").toIntOrNull() ?: return invalidInput()
            )
            staff.addMedia(catalog, book)
        }
        "Magazine" -> {
            val magazine = Magazine(
                title = prompt("Enter title:"),
                author = prompt("Enter author:"),
                publicationYear = prompt("Enter publication year:").toIntOrNull() ?: return invalidInput(),
                numberOfPages = prompt("Enter number of pages:").toIntOrNull() ?: return invalidInput(),
                countryOfIssue = prompt("Enter country of issue:")
            )
            staff.addMedia(catalog, magazine)
        }
        "EBook" -> {
            val eBook = EBook(
                title = prompt("Enter title:"),
                author = prompt("Enter author:"),
                publicationYear = prompt("Enter publication year:").toIntOrNull() ?: return invalidInput(),
                fileSize = prompt("Enter file size (MB):").toDoubleOrNull() ?: return invalidInput(),
                format = EBookFormat.valueOf(prompt("Enter format (PDF, EPUB, AZW, PLAIN_TEXT):"))
            )
            staff.addMedia(catalog, eBook)
        }
        "Audiobook" -> {
            val audiobook = Audiobook(
                title = prompt("Enter title:"),
                author = prompt("Enter author:"),
                publicationYear = prompt("Enter publication year:").toIntOrNull() ?: return invalidInput(),
                fileSize = prompt("Enter file size (MB):").toDoubleOrNull() ?: return invalidInput(),
                duration = prompt("Enter duration (minutes):").toIntOrNull() ?: return invalidInput(),
                narrator = prompt("Enter narrator's name:")
            )
            staff.addMedia(catalog, audiobook)
        }
        "DigitalMagazine" -> {
            val digitalMagazine = DigitalMagazine(
                title = prompt("Enter title:"),
                author = prompt("Enter author:"),
                publicationYear = prompt("Enter publication year:").toIntOrNull() ?: return invalidInput(),
                fileSize = prompt("Enter file size (MB):").toDoubleOrNull() ?: return invalidInput(),
                countryOfIssue = prompt("Enter country of issue:")
            )
            staff.addMedia(catalog, digitalMagazine)
        }
        else -> println("Invalid media type. Please try again.")
    }
}

fun removeMedia(catalog: Catalog, staff: StaffUser) {
    val title = prompt("Enter the title of the media to remove:")
    val media = catalog.search(title) { it.title.equals(title, ignoreCase = true) }.firstOrNull()
    if (media != null) {
        staff.removeMedia(catalog, media)
    } else {
        println("Media not found.")
    }
}

fun searchMedia(catalog: Catalog) {
    val query = prompt("Enter the title or author to search:")
    val results = catalog.search(query) { true }
    if (results.isNotEmpty()) {
        println("Search results:")
        results.forEach { println(it.getDetails()) }
    } else {
        println("No media found.")
    }
}

fun borrowMedia(catalog: Catalog) {
    val user = RegularUser(prompt("Enter your user ID:"), prompt("Enter your name:"), prompt("Are you a premium user? (yes/no):").equals("yes", ignoreCase = true))
    val title = prompt("Enter the title of the media to borrow:")
    val author = prompt("Enter the author's name:")
    val media = catalog.search(title) { it.title.equals(title, ignoreCase = true) && it.author.equals(author, ignoreCase = true) }.firstOrNull()

    if (media != null) {
        user.borrowMedia(media)
    } else {
        println("Media not found or not available.")
    }
}

fun returnMedia(catalog: Catalog) {
    val user = RegularUser(prompt("Enter your user ID:"), prompt("Enter your name:"), prompt("Are you a premium user? (yes/no):").equals("yes", ignoreCase = true))
    val title = prompt("Enter the title of the media to return:")
    val media = catalog.search(title) { it.title.equals(title, ignoreCase = true) }.firstOrNull()

    if (media != null) {
        user.returnMedia(media)
    } else {
        println("Media not found.")
    }
}

fun getRecommendations(catalog: Catalog) {
    val user = RegularUser(prompt("Enter your user ID:"), prompt("Enter your name:"), prompt("Are you a premium user? (yes/no):").equals("yes", ignoreCase = true))
    println("Enter the type of media for recommendations (Book, Magazine, EBook, Audiobook, DigitalMagazine):")
    val mediaType = when (readLine() ?: "") {
        "Book" -> Book::class
        "Magazine" -> Magazine::class
        "EBook" -> EBook::class
        "Audiobook" -> Audiobook::class
        "DigitalMagazine" -> DigitalMagazine::class
        else -> return println("Invalid media type.")
    }

    val recommendations = user.getRecommendations(catalog, mediaType)
    if (recommendations.isNotEmpty()) {
        println("Recommendations:")
        recommendations.forEach { println(it.getDetails()) }
    } else {
        println("No recommendations available.")
    }
}

fun viewMediaCount(catalog: Catalog, staff: StaffUser) {
    val counts = staff.getMediaCount(catalog)
    println("Media count by type:")
    counts.forEach { (type, count) ->
        println("${type.simpleName}: $count")
    }
}

fun editMediaPrompt(catalog: Catalog, staff: StaffUser) {
    val title = prompt("Enter the title of the media to edit:")
    val media = catalog.search(title) { it.title.equals(title, ignoreCase = true) }.firstOrNull()

    if (media != null) {
        val newTitle = prompt("Enter new title (leave blank to keep current):")
        val newAuthor = prompt("Enter new author (leave blank to keep current):")
        val newYear = prompt("Enter new publication year (leave blank to keep current):").toIntOrNull() ?: media.publicationYear

        val newMedia = when (media) {
            is Book -> {
                val newPages = prompt("Enter new page count (leave blank to keep current):").toIntOrNull() ?: media.numberOfPages
                Book(newTitle.ifBlank { media.title }, newAuthor.ifBlank { media.author }, newYear, newPages)
            }
            is Magazine -> {
                val newNumberOfPages = prompt("Enter new number of pages (leave blank to keep current):").toIntOrNull() ?: media.numberOfPages
                val newCountryOfIssue = prompt("Enter new country of issue (leave blank to keep current):").ifBlank { media.countryOfIssue }
                Magazine(newTitle.ifBlank { media.title }, newAuthor.ifBlank { media.author }, newYear, newNumberOfPages, newCountryOfIssue)
            }
            is EBook -> {
                val newFileSize = prompt("Enter new file size (leave blank to keep current):").toDoubleOrNull() ?: media.fileSize
                val newFormat = EBookFormat.valueOf(prompt("Enter new format (leave blank to keep current):").ifBlank { media.format.name })
                EBook(newTitle.ifBlank { media.title }, newAuthor.ifBlank { media.author }, newYear, newFileSize, newFormat)
            }
            is Audiobook -> {
                val newFileSize = prompt("Enter new file size (leave blank to keep current):").toDoubleOrNull() ?: media.fileSize
                val newDuration = prompt("Enter new duration (leave blank to keep current):").toIntOrNull() ?: media.duration
                val newNarrator = prompt("Enter new narrator (leave blank to keep current):").ifBlank { media.narrator }
                Audiobook(newTitle.ifBlank { media.title }, newAuthor.ifBlank { media.author }, newYear, newFileSize, newDuration, newNarrator)
            }
            is DigitalMagazine -> {
                val newFileSize = prompt("Enter new file size (leave blank to keep current):").toDoubleOrNull() ?: media.fileSize
                val newCountryOfIssue = prompt("Enter new country of issue (leave blank to keep current):").ifBlank { media.countryOfIssue }
                DigitalMagazine(newTitle.ifBlank { media.title }, newAuthor.ifBlank { media.author }, newYear, newFileSize, newCountryOfIssue)
            }
            else -> null
        }

        if (newMedia != null) {
            staff.editMedia(catalog, media, newMedia)
        } else {
            println("Failed to update media.")
        }
    } else {
        println("Media not found.")
    }
}

fun prompt(message: String): String {
    print(message)
    return readLine() ?: ""
}

fun invalidInput() {
    println("Invalid input. Please try again.")
}