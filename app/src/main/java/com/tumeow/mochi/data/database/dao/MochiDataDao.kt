package com.tumeow.mochi.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tumeow.mochi.data.database.dataentities.MochiData

@Dao
interface MochiDataDao {

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: MochiData)

    @Update
    suspend fun update(data: MochiData)

    @Delete
    suspend fun delete(data: MochiData)

    //SELECT(要抓取的欄位) *(全部) FROM 從哪張表格 ORDER BY(以什麼來排序) 什麼東西 ASC(預設:正序)/DESC(倒序)
    @Query("SELECT * FROM mochi_data ORDER BY date DESC LIMIT 30")
    suspend fun searchAllData(): List<MochiData>

    //WHERE(篩選條件) date(資料表裡的欄位) = :date(fun代入的參數)，LIKE跟=很像，但可以搭配%做模糊搜尋(參考Repository)
    @Query("SELECT * FROM mochi_data WHERE item LIKE :itemPrice OR price LIKE :itemPrice ORDER BY date DESC")
    suspend fun searchData(itemPrice: String): List<MochiData>

    //WHERE(篩選條件) date(資料表裡的欄位) = :date(fun代入的參數)，LIKE跟=很像，但可以搭配%做模糊搜尋(參考Repository)
    @Query("SELECT * FROM mochi_data WHERE date = :date OR item LIKE :itemPrice OR price LIKE :itemPrice ORDER BY date DESC")
    suspend fun searchData(date: String, itemPrice: String): List<MochiData>
}