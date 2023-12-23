package com.example.massapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class, CardInfo::class], version = 3)
abstract class MassAppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun cardInfoDao(): CardInfoDao

    companion object {
        @Volatile
        private var instance: MassAppDatabase? = null

        fun getInstance(context: Context): MassAppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): MassAppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                MassAppDatabase::class.java,
                "massapp-database"
            )
                .build()
        }
    }
}
