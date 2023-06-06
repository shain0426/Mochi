package com.tumeow.mochi.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

//序列化
@Parcelize
data class MochiItemEntity(

    val itemDate : String = "",
    val item : String = "",
    val itemPrice : String = ""
) : Parcelable
