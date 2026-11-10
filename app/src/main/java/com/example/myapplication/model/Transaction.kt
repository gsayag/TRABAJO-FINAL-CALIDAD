package com.example.myapplication.model

import androidx.compose.ui.graphics.Color

enum class TransactionType(val value: String) {
    INCOME("income"),
    EXPENSE("expense");

    companion object {
        fun fromValue(value: String): TransactionType =
            values().find { it.value.equals(value, ignoreCase = true) } ?: EXPENSE
    }
}

enum class ExpenseCategory(val displayName: String, val symbol: String, val color: Color) {
    ALIMENTACION("Alimentación", "◈", Color(0xFFA2FA65)),
    TRANSPORTE("Transporte", "↗", Color(0xFF70CCED)),
    SERVICIOS("Servicios", "⌁", Color(0xFFFFD079)),
    COMPRAS("Compras", "▣", Color(0xFFCEACFF)),
    ENTRETENIMIENTO("Entretenimiento", "✦", Color(0xFFFF9EB6)),
    SALUD("Salud", "✚", Color(0xFF7DE3CC)),
    OTROS("Otros", "◇", Color(0xFF8F9EA9));

    companion object {
        fun fromString(name: String): ExpenseCategory =
            entries.find { it.displayName.equals(name, ignoreCase = true) } ?: OTROS
    }
}

data class Transaction(
    val id: String,
    val type: TransactionType,
    val amount: Double,
    val category: String, // "Ingresos" or one of ExpenseCategory.displayName
    val description: String,
    val date: String // YYYY-MM-DD
) {
    val symbol: String
        get() = if (type == TransactionType.INCOME) "↓" else ExpenseCategory.fromString(category).symbol

    val color: Color
        get() = if (type == TransactionType.INCOME) Color(0xFFA2FA65) else ExpenseCategory.fromString(category).color
}
