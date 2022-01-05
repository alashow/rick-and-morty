/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.domain.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import tm.alashow.domain.models.BasePaginatedEntity

typealias LocationId = Long

@Parcelize
@Serializable
data class Location(
    @SerialName("id")
    @ColumnInfo(name = "id")
    val id: CharacterId = 0L,

    @SerialName("name")
    @ColumnInfo(name = "name")
    val name: String = UNKNOWN,

    @SerialName("dimension")
    @ColumnInfo(name = "dimension")
    val dimension: String = "",

    @SerialName("type")
    @ColumnInfo(name = "type")
    val type: String = "",

    @SerialName("residents")
    @ColumnInfo(name = "residents")
    val residents: List<String> = emptyList(),

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
}
