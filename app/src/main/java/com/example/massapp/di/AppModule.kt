package com.example.massapp.di

import android.app.Application
import androidx.room.Room
import com.example.massapp.room.MassAppDatabase
import com.example.massapp.room.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMassAppDatabase(application: Application): MassAppDatabase {
        return Room.databaseBuilder(
            application,
            MassAppDatabase::class.java,
            "massapp-database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideUserDao(appDatabase: MassAppDatabase): UserDao {
        return appDatabase.userDao()
    }
}