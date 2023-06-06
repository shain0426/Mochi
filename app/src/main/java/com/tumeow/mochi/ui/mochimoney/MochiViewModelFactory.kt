package com.tumeow.mochi.ui.mochimoney

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tumeow.mochi.data.repositories.MochiRepository

class MochiViewModelFactory (private val mochiRepository: MochiRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) = MochiViewModel() as T

}