/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import tm.alashow.rickmorty.domain.models.errors.ApiErrorException

interface ApiResponse {
    val isSuccessful: Boolean
}

@Serializable
abstract class PaginatedApiResponse() : ApiResponse {

    @Serializable
    data class PaginationInfo(
        @SerialName("count")
        val totalItems: Int = 0,

        @SerialName("pages")
        val pages: Int = 0,

        @SerialName("next")
        val nextPageUrl: String? = null,

        @SerialName("prev")
        val previousPageUrl: String? = null,
    )
}

fun <T : ApiResponse> T.checkForErrors(): T = if (isSuccessful) this
else throw ApiErrorException()
