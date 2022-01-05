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
import tm.alashow.domain.models.BasePaginatedEntity

typealias CharacterId = Long

const val UNKNOWN = "Unknown"

@Parcelize
@Serializable
@Entity(tableName = "characters")
data class Character(
    @SerialName("id")
    @ColumnInfo(name = "id")
    val id: CharacterId = 0L,

    @SerialName("name")
    @ColumnInfo(name = "name")
    val name: String = UNKNOWN,

    @SerialName("status")
    @ColumnInfo(name = "status")
    val status: String = UNKNOWN,

    @SerialName("species")
    @ColumnInfo(name = "species")
    val species: String = UNKNOWN,

    @SerialName("gender")
    @ColumnInfo(name = "gender")
    val gender: String = UNKNOWN,

    @SerialName("type")
    @ColumnInfo(name = "type")
    val type: String = "",

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

    override fun getIdentifier() = id.toString()
}
