package users

import catalog.Catalog
import media_management.Media
import kotlin.reflect.KClass

class StaffUser(
    userId: String,
    name: String
) : User(userId, name) {
    fun addMedia(catalog: Catalog, media: Media) {
        catalog.addMedia(media)
        println("Media '${media.title}' added successfully by $name.")
    }

    fun removeMedia(catalog: Catalog, media: Media): Boolean {
        return if (catalog.removeMedia(media)) {
            println("Media '${media.title}' removed successfully by $name.")
            true
        } else {
            println("Media '${media.title}' could not be found in the catalog.")
            false
        }
    }

    fun editMedia(catalog: Catalog, oldMedia: Media, newMedia: Media): Boolean {
        return if (catalog.updateMedia(oldMedia, newMedia)) {
            println("Media '${oldMedia.title}' updated successfully by $name.")
            true
        } else {
            println("Media '${oldMedia.title}' could not be found in the catalog.")
            false
        }
    }

    fun getMediaCount(catalog: Catalog): Map<KClass<out Media>, Int> {
        return catalog.getMediaCountByType()
    }

    override fun searchMedia(catalog: Catalog, query: String, filter: (Media) -> Boolean): List<Media> {
        return catalog.search(query, filter)
    }
}