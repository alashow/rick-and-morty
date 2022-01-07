/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.domain.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import tm.alashow.domain.extensions.interpunctize
import tm.alashow.domain.models.BasePaginatedEntity
import tm.alashow.rickmorty.domain.isUnknown

typealias CharacterId = Long

const val UNKNOWN_ITEM = "Unknown"

@Parcelize
@Serializable
@Entity(tableName = "characters")
data class Character(
    @SerialName("id")
    @ColumnInfo(name = "id")
    val id: CharacterId = 0L,

    @SerialName("name")
    @ColumnInfo(name = "name")
    val name: String = UNKNOWN_ITEM,

    @SerialName("status")
    @ColumnInfo(name = "status")
    val status: String = UNKNOWN_ITEM,

    @SerialName("species")
    @ColumnInfo(name = "species")
    val species: String = UNKNOWN_ITEM,

    @SerialName("type")
    @ColumnInfo(name = "type")
    val type: String = "",

    @SerialName("gender")
    @ColumnInfo(name = "gender")
    val gender: String = UNKNOWN_ITEM,

    @SerialName("image")
    @ColumnInfo(name = "image")
    val imageUrl: String = "",

    @SerialName("url")
    @ColumnInfo(name = "url")
    val url: String = "",

    @SerialName("origin")
    @ColumnInfo(name = "origin")
    val origin: Location = Location(),

    @SerialName("location")
    @ColumnInfo(name = "location")
    val location: Location = Location(),

    @Transient
    @ColumnInfo(name = "details_fetched")
    val detailsFetched: Boolean = false,

    @SerialName("params")
    @ColumnInfo(name = "params")
    override var params: String = defaultParams,

    @SerialName("page")
    @ColumnInfo(name = "page")
    override var page: Int = defaultPage,

    @PrimaryKey
    val primaryKey: String = "",

    @SerialName("search_index")
    @ColumnInfo(name = "search_index")
    val searchIndex: Int = 0,
) : BasePaginatedEntity(), Parcelable {

    val description
        get() = listOf(status, species, type)
            .filterNot { it.isUnknown() }
            .interpunctize(" - ")
            .ifBlank { "No description" }

    val isAlive get() = status.lowercase() == "alive"
    val isDead get() = status.lowercase() == "dead"

    override fun getIdentifier() = id.toString()

    fun isUnknown() = name.startsWith("$UNKNOWN_ITEM #")

    companion object {
        fun createUnknown(id: CharacterId) = Character(
            id = id,
            name = "$UNKNOWN_ITEM #$id",
        )
    }
}
