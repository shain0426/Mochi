package com.tumeow.mochi.ui.mochimoney.mochimoneyadd

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.tumeow.mochi.R
import com.tumeow.mochi.data.database.dataentities.MochiData
import com.tumeow.mochi.databinding.FragmentMochiAddBinding
import com.tumeow.mochi.ui.base.BaseFragment
import org.kodein.di.instance

class MochiAddFragment : BaseFragment() {

    private lateinit var addBinding: FragmentMochiAddBinding
    private val addFactory by instance<MochiAddViewModelFactory>()
    private val addViewModel by viewModels<MochiAddViewModel> {addFactory}


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        addBinding = FragmentMochiAddBinding.inflate(inflater, container, false).apply {

            lifecycleOwner = viewLifecycleOwner
            addEntity = addViewModel.addEntity
            addOnClickbt = this@MochiAddFragment

        }

        //抓出上個頁面傳來的資料，name要參考MochiActivity.jumpAddPage跳頁時放入的資料名稱
        /* 新舊版本抓上個頁面傳來資料的方式不太一樣，用if判斷版本去找到相應的抓資料fun */
        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("oldData", MochiData::class.java)
        }
        else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable("oldData") as MochiData?
        }


        /* data != null，表示上個頁面有傳資料來，所以是修改 */
        //把上個頁面傳來的MochiData暫存在ViewModel，然後修改介面(輸入框放入資料&title和bt改成"修改")，可以在ViewModel寫個fun做這些事
        if (data != null) {

            addViewModel.putUiOldData(data)

            addViewModel.addEntity.reviseBT.value = getString(R.string.update)

        }
        else {

            addViewModel.addEntity.reviseBT.value = getString(R.string.add)
        }



        addViewModel.snackBarMessage.observe(viewLifecycleOwner) {

            when (it) {

                MochiAddViewModel.ADD_SUCCESS -> {
                    //TODO 改顯示文字
                    Snackbar.make(addBinding.root, getString(R.string.addSuccess), Snackbar.LENGTH_LONG).show()
                }

                MochiAddViewModel.ADD_REVISE -> {
                    //TODO 改顯示文字
                    Snackbar.make(addBinding.root, getString(R.string.addRevise), Snackbar.LENGTH_LONG).show()
                }

                MochiAddViewModel.ADD_ITEMFALSE-> {
                    //TODO 改顯示文字
                    Snackbar.make(addBinding.root, getString(R.string.addItemFalse), Snackbar.LENGTH_LONG).show()
                }

                MochiAddViewModel.ADD_PRICEFALSE -> {
                    //TODO 改顯示文字
                    Snackbar.make(addBinding.root, getString(R.string.addPriceFalse), Snackbar.LENGTH_LONG).show()
                }
            }
        }
        //fragment才需要加這段
        return addBinding.root

    }

    override fun onClick(v: View) {
        super.onClick(v)

        when (v) {
            addBinding.addCalendar -> showPicker()
            addBinding.addItemButton -> addViewModel.pressAddReviseButton()
        }
    }

    private fun showPicker(){
        val picker = addViewModel.addDatePicker()
        picker.show(requireActivity().supportFragmentManager, "calendar")
    }


}