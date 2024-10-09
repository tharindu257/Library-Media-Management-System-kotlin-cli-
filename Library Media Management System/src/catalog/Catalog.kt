package catalog

import media_management.Media
import kotlin.reflect.KClass

class Catalog {
    private val mediaList = mutableListOf<Media>()

    fun addMedia(media: Media) {
        mediaList.add(media)
    }

    fun removeMedia(media: Media): Boolean {
        return mediaList.remove(media)
    }

    fun updateMedia(oldMedia: Media, newMedia: Media): Boolean {
        val index = mediaList.indexOf(oldMedia)
        return if (index != -1) {
            mediaList[index] = newMedia
            true
        } else {
            false
        }
    }

    fun search(query: String, filter: (Media) -> Boolean): List<Media> {
        return mediaList.filter(filter).filter {
            it.title.contains(query, ignoreCase = true) ||
                    it.author.contains(query, ignoreCase = true)
        }
    }

    fun getMediaCountByType(): Map<KClass<out Media>, Int> {
        return mediaList.groupBy { it::class }.mapValues { it.value.size }
    }
}