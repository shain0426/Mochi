package com.tumeow.mochi.ui.mochimoney.mochimoneyadd

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tumeow.mochi.data.repositories.MochiRepository


class MochiAddViewModelFactory(private val mochiRepository: MochiRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) = MochiAddViewModel(mochiRepository) as T

}