package com.tumeow.mochi.ui.mochimoney.mochimoneyhistory

import android.view.View
import com.tumeow.mochi.R
import com.tumeow.mochi.data.database.dataentities.MochiData
import com.tumeow.mochi.databinding.ItemMochiBinding
import com.xwray.groupie.viewbinding.BindableItem

class MochiItem (private val mochiData: MochiData, private val itemClick: ItemClick) : BindableItem<ItemMochiBinding>() {

    override fun bind(viewBinding: ItemMochiBinding, position: Int) {
        viewBinding.mochiData = mochiData
        viewBinding.itemClickbt = View.OnClickListener { itemClick.itemOnClick(mochiData, position) }
    }

    override fun getLayout() = R.layout.item_mochi

    override fun initializeViewBinding(view: View): ItemMochiBinding = ItemMochiBinding.bind(view)


    interface ItemClick{
        fun itemOnClick(data: MochiData, position: Int)
    }


}