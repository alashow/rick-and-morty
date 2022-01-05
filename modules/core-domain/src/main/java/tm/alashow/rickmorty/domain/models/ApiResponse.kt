/*
 * Copyright (C) 2018, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import tm.alashow.rickmorty.domain.entities.Character
import tm.alashow.rickmorty.domain.models.errors.ApiErrorException
import tm.alashow.rickmorty.domain.models.errors.mapToApiError

@Serializable
data class ApiResponse(
    @SerialName("status")
    val status: String,

    @SerialName("error")
    val error: Error? = null,

    @SerialName("data")
    val data: Data = Data(),
) {

    val isSuccessful get() = status == "ok"

    @Serializable
    data class Error(
        @SerialName("id")
        val id: String = "unknown",

        @SerialName("message")
        var message: String? = null,
    )

    @Serializable
    data class Data(
        @SerialName("message")
        val message: String = "",

        @SerialName("character")
        val character: Character = Character(),

        @SerialName("characters")
        val characters: List<Character> = arrayListOf(),
    )
}

fun ApiResponse.checkForErrors(): ApiResponse = if (isSuccessful) this
else throw ApiErrorException(error ?: ApiResponse.Error("unknown", "Unknown error"))
    .mapToApiError()
