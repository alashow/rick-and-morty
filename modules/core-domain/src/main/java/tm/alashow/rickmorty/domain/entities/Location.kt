/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.domain.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import tm.alashow.domain.models.BasePaginatedEntity
import tm.alashow.rickmorty.domain.isUnknown

typealias LocationId = Long

@Parcelize
@Serializable
data class Location(
    @SerialName("id")
    @ColumnInfo(name = "id")
    val id: LocationId = 0L,

    @SerialName("name")
    @ColumnInfo(name = "name")
    val name: String = UNKNOWN_ITEM,

    @SerialName("type")
    @ColumnInfo(name = "type")
    val type: String = "",

    @SerialName("dimension")
    @ColumnInfo(name = "dimension")
    val dimension: String = "",

    @SerialName("residents")
    @ColumnInfo(name = "residents")
    val residents: List<String> = emptyList(),

    @Ignore
    @Transient
    val residentsCharacters: List<Character> = emptyList(),

    @SerialName("url")
    @ColumnInfo(name = "url")
    val url: String = "",

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

    val isUnknown get() = name.isBlank() || name.isUnknown()
}
