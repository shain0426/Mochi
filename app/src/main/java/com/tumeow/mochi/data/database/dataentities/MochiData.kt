package com.tumeow.mochi.data.database.dataentities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "mochi_data"
)

data class MochiData(

    @PrimaryKey(autoGenerate = true)
    var id : Long?,

    var date: String,
    var item: String,
    var price: String
) : Parcelable
