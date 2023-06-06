package com.tumeow.mochi.data.entities

import androidx.lifecycle.MutableLiveData

data class MochiEntity(

    val square: MutableLiveData<String> = MutableLiveData(""),
    val title: MutableLiveData<String> = MutableLiveData("")
){}
