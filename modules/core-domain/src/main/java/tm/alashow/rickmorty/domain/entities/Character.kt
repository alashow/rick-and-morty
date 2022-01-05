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
import tm.alashow.rickmorty.domain.UNKNOWN_CHARACTER
import tm.alashow.domain.models.BasePaginatedEntity

typealias CharacterId = String

@Parcelize
@Serializable
@Entity(tableName = "characters")
data class Character(
    @SerialName("id")
    @ColumnInfo(name = "id")
    val id: CharacterId = "",

    @SerialName("name")
    @ColumnInfo(name = "name")
    val name: String = UNKNOWN_CHARACTER,

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

    override fun getIdentifier() = id
}
