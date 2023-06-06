package com.tumeow.mochi.ui

import android.app.Application
import com.tumeow.mochi.data.database.AppDatabase
import com.tumeow.mochi.data.repositories.MochiRepository
import com.tumeow.mochi.ui.mochimoney.MochiViewModelFactory
import com.tumeow.mochi.ui.mochimoney.mochimoneyadd.MochiAddViewModelFactory
import com.tumeow.mochi.ui.mochimoney.mochimoneyhistory.MochiHistoryViewModelFactory
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

class MochiApplication : Application(), DIAware {
    override val di by DI.lazy {

        import(androidXModule(this@MochiApplication))

        bind { singleton { AppDatabase(instance()) } }


        //Repository
        bind {singleton { MochiRepository(instance()) }}

        //ViewModelFactory
        bind { singleton { MochiHistoryViewModelFactory(instance()) }}
        bind { singleton { MochiAddViewModelFactory(instance()) }}
        bind { singleton { MochiViewModelFactory(instance()) }}



    }
}