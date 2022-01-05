/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.data

data class SearchParams(
    val query: String,
    val page: Int = 0,
) {

    // used as a key in Room/Store
    override fun toString() = "query=$query"

    companion object {
        fun SearchParams.toQueryMap(): Map<String, Any> = mutableMapOf<String, Any>(
            "query" to query,
            "page" to page,
        )
    }
}
