package com.example.massapp.retrofit

// Esquema de la respuesta para la solicitud de información de la tarjeta
data class CardResponse(
    val card: String,            // Número de la tarjeta
    val isValid: Boolean,        // Indica si la tarjeta es válida
    val status: String,          // Estado de la tarjeta
    val statusCode: Int          // Código de estado de la tarjeta
)

// Esquema de la respuesta para la solicitud de saldo de la tarjeta
data class BalanceResponse(
    val card: String,            // Número de la tarjeta
    val balance: Int,            // Saldo actual de la tarjeta
    val balanceDate: String?,    // Fecha del saldo actual
    val virtualBalance: Int,     // Saldo virtual de la tarjeta
    val virtualBalanceDate: String?  // Fecha del saldo virtual
)


