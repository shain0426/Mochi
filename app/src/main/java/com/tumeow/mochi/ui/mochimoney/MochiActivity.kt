package com.tumeow.mochi.ui.mochimoney

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import com.tumeow.mochi.R
import com.tumeow.mochi.databinding.ActivityMochiBinding
import com.tumeow.mochi.ui.base.BaseActivity
import org.kodein.di.instance

class MochiActivity: BaseActivity() {

    private val binding: ActivityMochiBinding by lazy { DataBindingUtil.setContentView(this, R.layout.activity_mochi) }
    private val factory by instance<MochiViewModelFactory>()
    private val viewModel by viewModels<MochiViewModel> { factory }

    private val fragment: NavHostFragment by lazy { supportFragmentManager.findFragmentById(R.id.mochi_fragment) as NavHostFragment }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(binding) {

            lifecycleOwner = this@MochiActivity
            entity = viewModel.entity
            addOnClickbt = this@MochiActivity

        }

        fragment.navController.addOnDestinationChangedListener { controller, destination, arguments ->

            when (destination.id) {
                R.id.list_fragment -> {
                    viewModel.entity.title.value = getString(R.string.titleMochi)
                    binding.returnButton.visibility = View.GONE
                }
                R.id.add_fragment -> {
                    viewModel.entity.title.value = getString(R.string.titleAdd)
                    binding.returnButton.visibility = View.VISIBLE
                }

            }

            //viewModel.entity.title.value = destination.label.toString()
        }
    }

    override fun onClick(v: View) {
        super.onClick(v)

        when(v){
            binding.returnButton -> fragment.navController.popBackStack()
        }
    }


}