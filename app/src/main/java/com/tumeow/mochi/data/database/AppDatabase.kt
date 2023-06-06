package com.tumeow.mochi.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tumeow.mochi.data.database.dao.MochiDataDao
import com.tumeow.mochi.data.database.dataentities.MochiData


@Database(
    entities = [MochiData::class],
    version = 1
)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getMochiDao() : MochiDataDao


    companion object {

        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context): AppDatabase = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context): AppDatabase = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "MyDataBase.db"
        ).build()
    }


}