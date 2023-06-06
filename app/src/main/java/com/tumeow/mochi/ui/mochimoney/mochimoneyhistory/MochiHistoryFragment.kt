package com.tumeow.mochi.ui.mochimoney.mochimoneyhistory

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tumeow.mochi.R
import com.tumeow.mochi.data.database.dataentities.MochiData
import com.tumeow.mochi.databinding.FragmentMochiMainListBinding
import com.tumeow.mochi.ui.base.BaseFragment
import org.kodein.di.instance

class MochiHistoryFragment : BaseFragment() {

    private lateinit var binding: FragmentMochiMainListBinding
    private val factory by instance<MochiHistoryViewModelFactory>()
    private val viewModel by viewModels<MochiHistoryViewModel> {factory}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {

        binding = FragmentMochiMainListBinding.inflate(inflater, container, false).apply {

            lifecycleOwner = viewLifecycleOwner
            entity = viewModel.entity
            onClickbt = this@MochiHistoryFragment

            with(mochiRecycler) {
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
                adapter = viewModel.adapter
            }
        }

        return binding.root

    }



//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        with(binding) {
//            lifecycleOwner = this@MochiHistoryFragment
//            entity = viewModel.entity
//            onClickbt = this@MochiHistoryFragment
//
//            with(mochiRecycler){
//                layoutManager = LinearLayoutManager(this@MochiHistoryFragment)
//                setHasFixedSize(true)
//                adapter = viewModel.adapter
//            }
//        }
//
//    }


    //切app或app裡的頁面，它都會再refresh一次
    override fun onResume() {
        super.onResume()

        viewModel.showAllRefreshData(itemClickListener)

    }

    private val itemClickListener = object : MochiItem.ItemClick{

        override fun itemOnClick(data: MochiData, position: Int) {

            AlertDialog.Builder(requireContext())
                .setMessage("${getString(R.string.datetime)}${data.date}\n${getString(R.string.itemTitle)}${data.item}\n${getString(R.string.priceTitle)}${data.price}\n")
                .setPositiveButton(getString(R.string.cancelBT)) { dialog, _ -> dialog.dismiss() }
                .setNegativeButton(getString(R.string.reviseBT)) { dialog, _ -> jumpAddPage(dialog,data) }
                .setNeutralButton(getString(R.string.deleteBT)) { dialog, _ -> deleteData(dialog, data, position) }
                .create()
                .show()

        }
    }

    private fun jumpAddPage(dialogInterface: DialogInterface,data: MochiData) {
        dialogInterface.dismiss()

        //跟Intent很像，同樣是把資料帶到下個頁面
        val bundle = Bundle()
        bundle.putParcelable("oldData", data)

        findNavController().navigate(R.id.action_list_fragment_to_add_fragment, bundle)
    }

    private fun deleteData(dialogInterface: DialogInterface, data: MochiData, position: Int) {
        dialogInterface.dismiss()
        viewModel.deleteItem(data, position)
    }


    override fun onClick(v: View) {
        super.onClick(v)

        when(v){
            binding.calendarIcon -> selectDate()
            binding.go -> viewModel.searchItem(itemClickListener)
            binding.showAll -> viewModel.showAllRefreshData(itemClickListener)
            binding.addButton -> addDataButton()
        }
    }



    private fun selectDate() {

        val picker = viewModel.getDatePicker(itemClickListener)

        //"ttt"是取他Picker(dialogue)的名字
        picker.show(requireActivity().supportFragmentManager, "ttt")
    }


    private fun addDataButton() {

        findNavController().navigate(R.id.action_list_fragment_to_add_fragment)
    }


}