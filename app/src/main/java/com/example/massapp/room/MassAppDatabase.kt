package com.example.massapp.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1)
abstract class MassAppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

}