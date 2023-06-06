package com.tumeow.mochi.ui.base

import android.R
import android.view.View
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import org.kodein.di.DIAware
import org.kodein.di.android.x.closestDI

abstract class BaseFragment : Fragment(), DIAware, View.OnClickListener {

    override val di by closestDI()

    override fun onClick(v: View) {

        WindowInsetsControllerCompat(requireActivity().window, requireActivity().findViewById(R.id.content)).hide(WindowInsetsCompat.Type.ime())
    }
}