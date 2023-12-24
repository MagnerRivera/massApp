package com.example.massapp.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

// Interfaz de acceso a datos (DAO) para la entidad User
@Dao
interface UserDao {
    // Inserta un objeto User en la base de datos
    @Insert
    fun insert(user: User)

    // Consulto un usuario por nombre de usuario y contraseña
    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    fun getUserByEmailAndPassword(username: String, password: String): User?
}

// Interfaz de acceso a datos (DAO) para la entidad CardInfo
@Dao
interface CardInfoDao {
    // Inserta un objeto CardInfo en la base de datos
    @Insert
    fun insertCardInfo(cardInfo: CardInfo)

    // Consulto información de tarjeta por número de tarjeta
    @Query("SELECT * FROM card_info WHERE card = :cardNumber")
    fun getCardInfoByCardNumber(cardNumber: String): CardInfo?

    // Consulto toda la información de la tarjeta almacenada
    @Query("SELECT * FROM card_info")
    fun getAllCardInfo(): List<CardInfo>

    // Elimina un objeto CardInfo de la base de datos
    @Delete
    fun deleteCardInfo(cardInfo: CardInfo)
}