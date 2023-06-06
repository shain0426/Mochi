package com.tumeow.mochi.ui.mochimoney.mochimoneyadd

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.CompositeDateValidator
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.tumeow.mochi.data.database.dataentities.MochiData
import com.tumeow.mochi.data.entities.MochiAddEntity
import com.tumeow.mochi.data.repositories.MochiRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class MochiAddViewModel(private val mochiRepository: MochiRepository) : ViewModel() {

    val addEntity = MochiAddEntity()

    private var chosenDate = LocalDate.now()

    //先把變數oldDate設成空的，因為不知道他的資料怎麼來
    //後續再去底下更改 oldDate = 代入需要的資料(也許在某些有寫的fun裡面有帶資料過來)
    private var oldData: MochiData? = null

    private var addDefaultDate: String = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

    val snackBarMessage: MutableLiveData<Int> = MutableLiveData(0)
    //不必對它做實例化，也可以拿到它裡面的數字
    companion object {
        const val ADD_SUCCESS = 1
        const val ADD_REVISE = 2
        const val ADD_ITEMFALSE = 3
        const val ADD_PRICEFALSE = 4
    }

    //viewModel一開始建立會跑的地方
    init {
        addEntity.addDate.value = addDefaultDate
    }


    fun addDatePicker(): MaterialDatePicker<Long> {

        val vali = CompositeDateValidator.allOf(listOf(DateValidatorPointBackward.now()))

        val con = CalendarConstraints.Builder()
            .setValidator(vali)
            .setEnd(LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli())
            .build()

        //.formst -> 轉成string
        //var defaultDate: String = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

        val pickerBuilder = MaterialDatePicker.Builder
            .datePicker()
            .setSelection(chosenDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli())
            .setCalendarConstraints(con)

        val picker = pickerBuilder.build().apply {

            //處理按下positive button後的動作，也就是說可以得到選好的日期
            //把Milisecond轉成localdatetime
            addOnPositiveButtonClickListener {

                val putDate: LocalDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneId.systemDefault()).toLocalDate()

                addEntity.addDate.value = putDate.toString()

                chosenDate = putDate
            }
        }

        return picker
    }

    //只有在修改的時候才會做: 做暫存資料&把舊資料放到介面的動作
    fun putUiOldData(data: MochiData) {

        //暫存是因為有其他地方會使用到這個data
        oldData = data

        //把data的date從String解析成LocalDate，暫存起來給picker設定selection
        chosenDate = LocalDate.parse(data.date)

        addEntity.addDate.value = data.date
        addEntity.addItem.value = data.item
        addEntity.addPrice.value = data.price

    }

    fun pressAddReviseButton() {
     /*判斷輸入框是否為空，如果是空的代表使用者沒放值進來，就不讓他執行下去
       但如果他不是空的，表示他有資料-->有資料的話=有可能是需要修改的舊資料 or 使用者輸入的新資料*/
        val addDate = addEntity.addDate.value!!
        //判斷是否為空，是的話就直接跳回呼叫此fun的地方，因為再執行下去會error
        val addItem = addEntity.addItem.value ?: return
        val addPrice = addEntity.addPrice.value ?: return

        //如果為空，不能再繼續執行下去，否則還是會跑到下面的insertMochiData
        if( addItem == "" ) {
            snackBarMessage.value = ADD_ITEMFALSE
            return
        }

        //如果為空，不能再繼續執行下去，否則還是會跑到下面的insertMochiData
        if( addPrice == "" ) {
            snackBarMessage.value = ADD_PRICEFALSE
            return
        }

        CoroutineScope(Dispatchers.IO).launch {

            //判斷是新增還是修改，是修改的話，把暫存的MochiData內容更新之後，用Dao的update做資料更新
            if (oldData != null) {

                val newData = oldData!!.apply {

                    date = addDate
                    item = addItem
                    price = addPrice
                }

                mochiRepository.updateData(newData)

                CoroutineScope(Dispatchers.Main).launch {

                    snackBarMessage.value = ADD_REVISE
                    addEntity.addItem.value = null
                    addEntity.addPrice.value = null
                }
            }
            else {
                mochiRepository.insertMochiData(addDate,addItem,addPrice)

                //把ADD_SUCCESS放入snackBar裡顯示，然後個別清空addItem&addPrice的值
                CoroutineScope(Dispatchers.Main).launch {

                    snackBarMessage.value = ADD_SUCCESS
                    addEntity.addItem.value = null
                    addEntity.addPrice.value = null
                }
            }

        }



    }

}