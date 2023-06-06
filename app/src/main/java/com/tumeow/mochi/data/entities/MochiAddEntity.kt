package com.tumeow.mochi.data.entities

import androidx.lifecycle.MutableLiveData

data class MochiAddEntity(

    val addDate : MutableLiveData<String> = MutableLiveData(""),
    val addItem : MutableLiveData<String> = MutableLiveData(""),
    val addPrice: MutableLiveData<String> = MutableLiveData(""),

    val reviseBT: MutableLiveData<String> = MutableLiveData(""),
){}
