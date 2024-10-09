package users

import catalog.Catalog
import media_management.Media

abstract class User(
    val userId: String,
    val name: String
) {
    abstract fun searchMedia(catalog: Catalog, query: String, filter: (Media) -> Boolean): List<Media>
}