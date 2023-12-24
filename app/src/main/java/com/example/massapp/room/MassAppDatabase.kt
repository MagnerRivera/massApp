package com.example.massapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Anotación que indica que esta clase representa la base de datos local con Room
@Database(entities = [User::class, CardInfo::class], version = 3)
abstract class MassAppDatabase : RoomDatabase() {

    // Métodos abstractos para obtener instancias de los DAO (Data Access Object)
    abstract fun userDao(): UserDao
    abstract fun cardInfoDao(): CardInfoDao

    companion object {
        // Objeto volátil que garantiza que el valor de 'instance' siempre será actualizado correctamente
        @Volatile
        private var instance: MassAppDatabase? = null

        // Método para obtener una instancia de la base de datos
        fun getInstance(context: Context): MassAppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        // Método privado para construir la instancia de la base de datos
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
