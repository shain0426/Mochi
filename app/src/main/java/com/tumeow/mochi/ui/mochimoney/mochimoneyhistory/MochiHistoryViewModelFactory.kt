package com.tumeow.mochi.ui.mochimoney.mochimoneyhistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tumeow.mochi.data.repositories.MochiRepository

class MochiHistoryViewModelFactory(private val mochiRepository: MochiRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) = MochiHistoryViewModel(mochiRepository) as T

}