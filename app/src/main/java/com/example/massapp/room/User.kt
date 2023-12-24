package com.example.massapp.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

// Anotación que indica que esta clase representa una entidad de la base de datos, es decir crear la tabla en la base de datos
@Entity(tableName = "users")
data class User(
    // Anotación que indica la clave primaria de la entidad
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val document: String,
    val password: String,
    val username: String,
    val address: String,
    val email: String,
)

// Anotación que indica que esta clase representa una entidad de la base de datos
@Entity(tableName = "card_info")
data class CardInfo(
    // Anotación que indica la clave primaria de la entidad
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val card: String,
    val balance: Int,
    val balanceDate: String
)
