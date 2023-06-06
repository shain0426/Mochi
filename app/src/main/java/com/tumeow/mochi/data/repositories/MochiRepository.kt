package com.tumeow.mochi.data.repositories

import com.tumeow.mochi.data.database.AppDatabase
import com.tumeow.mochi.data.database.dataentities.MochiData
import java.time.LocalDate

class MochiRepository(private val db: AppDatabase) {

    suspend fun insertMochiData(date:String, item:String, price:String) {

        val data = MochiData(
            id = null,
            date = date,
            item = item,
            price = price
        )

        db.getMochiDao().insert(data)
    }

    suspend fun getMochiData(datePair: Pair<LocalDate, LocalDate>?, itemPrice: String): List<MochiData> {

        val resultSet = mutableSetOf<MochiData>()
        val input = when {
            //加%是為了模糊搜尋
            itemPrice != "" -> "%$itemPrice%"
            else -> ""
        }

        if (datePair == null) {

            val searchList = db.getMochiDao().searchData(input)
            resultSet.addAll(searchList)
        }
        else {

            var date = datePair.second

            do {

                val searchList = db.getMochiDao().searchData(date.toString(), input)
                resultSet.addAll(searchList)

                date = date.minusDays(1L)

            } while (!date.isBefore(datePair.first))
        }

        return resultSet.toList()
    }

    suspend fun deleteMochiData(data: MochiData) = db.getMochiDao().delete(data)

    suspend fun getAllData(): List<MochiData> = db.getMochiDao().searchAllData()

    suspend fun updateData(newData: MochiData) = db.getMochiDao().update(newData)
}