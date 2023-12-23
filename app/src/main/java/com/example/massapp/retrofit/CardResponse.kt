package com.example.massapp.retrofit

data class CardResponse(
    val card: String,
    val isValid: Boolean,
    val status: String,
    val statusCode: Int
)

data class BalanceResponse(
    val card: String,
    val balance: Int,
    val balanceDate: String?,
    val virtualBalance: Int,
    val virtualBalanceDate: String?
)


