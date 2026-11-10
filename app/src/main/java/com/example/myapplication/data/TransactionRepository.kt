package com.example.myapplication.data

import android.content.Context
import android.content.SharedPreferences
import com.example.myapplication.model.Transaction
import com.example.myapplication.model.TransactionType
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

class TransactionRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("fuga_prefs", Context.MODE_PRIVATE)

    private val KEY_TRANSACTIONS = "fuga-mvp-transactions-v1"

    val demoTransactions = listOf(
        Transaction("demo-1", TransactionType.INCOME, 3800.0, "Ingresos", "Sueldo mensual", "2026-11-10"),
        Transaction("demo-2", TransactionType.EXPENSE, 18.5, "Transporte", "Taxi al trabajo", "2026-11-09"),
        Transaction("demo-3", TransactionType.EXPENSE, 39.9, "Entretenimiento", "Streaming mensual", "2026-11-08"),
        Transaction("demo-4", TransactionType.EXPENSE, 186.4, "Alimentación", "Compras del mercado", "2026-11-07"),
        Transaction("demo-5", TransactionType.EXPENSE, 89.9, "Servicios", "Internet hogar", "2026-11-06"),
        Transaction("demo-6", TransactionType.EXPENSE, 38.5, "Alimentación", "Almuerzo", "2026-11-05"),
        Transaction("demo-7", TransactionType.EXPENSE, 47.2, "Servicios", "Recibo de agua", "2026-11-03"),
        Transaction("demo-8", TransactionType.INCOME, 900.0, "Ingresos", "Trabajo freelance", "2026-11-02"),
        Transaction("demo-9", TransactionType.EXPENSE, 145.0, "Compras", "Accesorios personales", "2026-11-01"),
        Transaction("demo-10", TransactionType.EXPENSE, 74.0, "Transporte", "Combustible", "2026-10-29")
    )

    fun listTransactions(): List<Transaction> {
        val jsonString = prefs.getString(KEY_TRANSACTIONS, null)
        if (jsonString == null) {
            saveTransactions(demoTransactions)
            return demoTransactions
        }
        return try {
            val jsonArray = JSONArray(jsonString)
            val list = mutableListOf<Transaction>()
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                list.add(
                    Transaction(
                        id = obj.getString("id"),
                        type = TransactionType.fromValue(obj.getString("type")),
                        amount = obj.getDouble("amount"),
                        category = obj.getString("category"),
                        description = obj.getString("description"),
                        date = obj.getString("date")
                    )
                )
            }
            list
        } catch (e: Exception) {
            demoTransactions
        }
    }

    fun addTransaction(
        type: TransactionType,
        amount: Double,
        category: String,
        description: String,
        date: String
    ): Transaction {
        val current = listTransactions().toMutableList()
        val newTx = Transaction(
            id = "m-${System.currentTimeMillis()}-${UUID.randomUUID().toString().take(8)}",
            type = type,
            amount = amount,
            category = category,
            description = description,
            date = date
        )
        current.add(newTx)
        saveTransactions(current)
        return newTx
    }

    fun deleteTransaction(id: String) {
        val current = listTransactions().filter { it.id != id }
        saveTransactions(current)
    }

    fun resetToDemo() {
        saveTransactions(demoTransactions)
    }

    private fun saveTransactions(transactions: List<Transaction>) {
        val jsonArray = JSONArray()
        for (tx in transactions) {
            val obj = JSONObject()
            obj.put("id", tx.id)
            obj.put("type", tx.type.value)
            obj.put("amount", tx.amount)
            obj.put("category", tx.category)
            obj.put("description", tx.description)
            obj.put("date", tx.date)
            jsonArray.put(obj)
        }
        prefs.edit().putString(KEY_TRANSACTIONS, jsonArray.toString()).apply()
    }
}
