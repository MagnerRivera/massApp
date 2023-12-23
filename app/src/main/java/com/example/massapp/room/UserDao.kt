package com.example.massapp.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    fun insert(user: User)

    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    fun getUserByEmailAndPassword(username: String, password: String): User?
}

@Dao
interface CardInfoDao {
    @Insert
    fun insertCardInfo(cardInfo: CardInfo)

    @Query("SELECT * FROM card_info WHERE card = :cardNumber")
    fun getCardInfoByCardNumber(cardNumber: String): CardInfo?

    @Query("SELECT * FROM card_info")
    fun getAllCardInfo(): List<CardInfo>

    @Delete
    fun deleteCardInfo(cardInfo: CardInfo)
}