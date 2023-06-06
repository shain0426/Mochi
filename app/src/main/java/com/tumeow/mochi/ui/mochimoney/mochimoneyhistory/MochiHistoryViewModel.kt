package com.tumeow.mochi.ui.mochimoney.mochimoneyhistory

import android.util.Log
import androidx.core.util.toAndroidXPair
import androidx.lifecycle.ViewModel
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.CompositeDateValidator
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.tumeow.mochi.data.database.dataentities.MochiData
import com.tumeow.mochi.data.entities.MochiEntity
import com.tumeow.mochi.data.repositories.MochiRepository
import com.xwray.groupie.GroupieAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

class MochiHistoryViewModel(private val mochiRepository: MochiRepository) : ViewModel() {

    val entity = MochiEntity()

    private var chosenDate: Pair<LocalDate, LocalDate>? = null

    val adapter = GroupieAdapter()


    fun deleteItem(data: MochiData, position: Int) {

        CoroutineScope(Dispatchers.IO).launch {

            mochiRepository.deleteMochiData(data)

            adapter.removeGroupAtAdapterPosition(position)
        }
    }

    fun getDatePicker(itemClick: MochiItem.ItemClick): MaterialDatePicker<androidx.core.util.Pair<Long, Long>> {

        //Validator:設定可選擇的日期範圍
        //allOf意思是 所選日期需要達成list裡所有的條件
        //DateValidatorPointBackward.now() --> 最後期限->當天(now)
        //Backward:下限(最後)  forward:上限(起始)
        val vali = CompositeDateValidator.allOf(listOf(DateValidatorPointBackward.now()))

        //設定可選範圍: setEnd指的是"可顯示的最後一個月份"，不管幾號他只看月
        val con = CalendarConstraints.Builder()
            .setValidator(vali)
            .setEnd(LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli())
            .build()

        val pickerBuilder = MaterialDatePicker.Builder
            .dateRangePicker()
            .apply {

                val datePair = chosenDate

                if (datePair != null) {

                    val range = Pair(
                        //localDate.atStartOfDay() -> 把localDate轉成localDateTime，後面再轉成Milisecond
                        datePair.first.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli(),
                        datePair.second.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()
                    )

                    //setSelection:打開picker後，會顯示的已選擇日期(ex: 使用者前一筆輸入完留下的日期)
                    setSelection(range.toAndroidXPair())
                }
            }
            //設定可選範圍
            .setCalendarConstraints(con)

        val picker = pickerBuilder.build().apply {

            //處理按下positive button後的動作，也就是說可以得到選好的日期
            //把Milisecond轉成localdatetime
            //這裡的it是使用者選擇完日期的結果
            addOnPositiveButtonClickListener {

                chosenDate = Pair(
                    LocalDateTime.ofInstant(Instant.ofEpochMilli(it.first), ZoneId.systemDefault()).toLocalDate(),
                    LocalDateTime.ofInstant(Instant.ofEpochMilli(it.second), ZoneId.systemDefault()).toLocalDate()
                )

                searchItem(itemClick)
            }
        }

        return picker
    }

    fun searchItem(itemClick: MochiItem.ItemClick) {
        //篩選條件設定
        val itemPrice = entity.square.value ?: return

        CoroutineScope(Dispatchers.IO).launch {
            //把設定好的篩選條件代入db的searchData()，然後放進list這個參數裡
            val list = mochiRepository.getMochiData(chosenDate, itemPrice)

            Log.e("-----", "list = $list")

            //轉換為adapter可用的相容
            val itemList = list.map { MochiItem(it, itemClick) }

            CoroutineScope(Dispatchers.Main).launch {

                adapter.clear()
                adapter.addAll(itemList)
            }
        }

    }

    fun showAllRefreshData(itemClick: MochiItem.ItemClick) {

        entity.square.value = null

        chosenDate = null

        CoroutineScope(Dispatchers.IO).launch {

            val allList = mochiRepository.getAllData()

            val allItemList = allList.map { MochiItem(it, itemClick) }

            CoroutineScope(Dispatchers.Main).launch {
                adapter.clear()
                adapter.addAll(allItemList)
            }
        }

    }



}