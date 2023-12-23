package com.example.massapp.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val document: String,
    val password: String,
    val username: String,
    val address: String,
    val email: String,
)

@Entity(tableName = "card_info")
data class CardInfo(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val card: String,
    val balance: Int,
    val balanceDate: String
)
