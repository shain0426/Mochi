package com.tumeow.mochi.ui.base

import android.R
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI

abstract class BaseActivity : AppCompatActivity() , DIAware , View.OnClickListener {
    override val di by closestDI()


    //在每次點擊時都把鍵盤收起來
    override fun onClick(v: View) {

        WindowInsetsControllerCompat(window, findViewById(R.id.content)).hide(WindowInsetsCompat.Type.ime())
    }
}