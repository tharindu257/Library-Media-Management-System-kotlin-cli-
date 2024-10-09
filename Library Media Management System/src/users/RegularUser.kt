package users

import catalog.Catalog
import media_management.Media
import kotlin.reflect.KClass

class RegularUser(
    userId: String,
    name: String,
    val isPremium: Boolean = false
) : User(userId, name) {
    private val borrowedMedia = mutableListOf<Media>()
    private val history = mutableListOf<Media>()

    fun borrowMedia(media: Media): Boolean {
        return if (media.isAvailable) {
            media.isAvailable = false
            val borrowDuration = if (isPremium) 14 else 7
            media.returnDate = "Due in $borrowDuration days"
            borrowedMedia.add(media)
            history.add(media)
            println("$name borrowed '${media.title}' successfully.")
            true
        } else {
            println("Sorry, '${media.title}' is not available for borrowing.")
            false
        }
    }

    fun returnMedia(media: Media): Boolean {
        return if (borrowedMedia.contains(media)) {
            media.isAvailable = true
            media.returnDate = null
            borrowedMedia.remove(media)
            println("$name returned '${media.title}' successfully.")
            true
        } else {
            println("$name has not borrowed '${media.title}'.")
            false
        }
    }

    fun getRecommendations(catalog: Catalog, mediaType: KClass<out Media>): List<Media> {
        val recommendedMedia = history.filterIsInstance(mediaType.java)
        return if (recommendedMedia.isNotEmpty()) {
            recommendedMedia
        } else {
            catalog.search("", { it::class == mediaType })
        }
    }

    override fun searchMedia(catalog: Catalog, query: String, filter: (Media) -> Boolean): List<Media> {
        return catalog.search(query, filter)
    }
}