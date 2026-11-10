package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.model.Transaction
import com.example.myapplication.model.TransactionType
import com.example.myapplication.ui.components.TransactionItem
import com.example.myapplication.ui.theme.FugaBorder
import com.example.myapplication.ui.theme.FugaGreen
import com.example.myapplication.ui.theme.FugaMuted
import com.example.myapplication.ui.theme.FugaPanel
import com.example.myapplication.ui.theme.FugaText

@Composable
fun HistoryScreen(
    transactions: List<Transaction>,
    onDeleteTransaction: (String) -> Unit
) {
    var search by remember { mutableStateOf("") }
    var filter by remember { mutableStateOf("all") } // "all", "expense", "income"
    var transactionToDelete by remember { mutableStateOf<Transaction?>(null) }

    val filtered = remember(transactions, search, filter) {
        transactions.sortedWith(compareByDescending<Transaction> { it.date }.thenByDescending { it.id })
            .filter { t ->
                val matchesFilter = when (filter) {
                    "expense" -> t.type == TransactionType.EXPENSE
                    "income" -> t.type == TransactionType.INCOME
                    else -> true
                }
                val matchesSearch = search.isBlank() ||
                        t.description.contains(search, ignoreCase = true) ||
                        t.category.contains(search, ignoreCase = true)
                matchesFilter && matchesSearch
            }
    }

    if (transactionToDelete != null) {
        AlertDialog(
            onDismissRequest = { transactionToDelete = null },
            title = { Text(text = "Eliminar movimiento", color = FugaText, fontWeight = FontWeight.Bold) },
            text = { Text(text = "¿Eliminar \"${transactionToDelete?.description}\"?", color = FugaMuted) },
            confirmButton = {
                TextButton(
                    onClick = {
                        transactionToDelete?.let { onDeleteTransaction(it.id) }
                        transactionToDelete = null
                    }
                ) {
                    Text(text = "Eliminar", color = Color(0xFFFFAD9C), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { transactionToDelete = null }) {
                    Text(text = "Cancelar", color = FugaMuted)
                }
            },
            containerColor = FugaPanel
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        // Heading
        Text(
            text = "CONTROL DE MOVIMIENTOS",
            color = FugaGreen,
            fontSize = 10.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.5.sp,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        Text(
            text = "Tu actividad",
            color = FugaText,
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        Text(
            text = "Consulta y elimina tus movimientos. Los datos se guardan localmente.",
            color = FugaMuted,
            fontSize = 12.sp,
            lineHeight = 19.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Search Input
        OutlinedTextField(
            value = search,
            onValueChange = { search = it },
            placeholder = { Text("Buscar descripción o categoría", color = Color(0xFF83909C), fontSize = 14.sp) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = FugaPanel,
                unfocusedContainerColor = FugaPanel,
                focusedBorderColor = FugaGreen,
                unfocusedBorderColor = Color(0xFF3D4650),
                focusedTextColor = FugaText,
                unfocusedTextColor = FugaText
            ),
            shape = RoundedCornerShape(12.dp)
        )

        // Filter Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("all" to "Todos", "expense" to "Gastos", "income" to "Ingresos").forEach { (key, label) ->
                val isActive = filter == key
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isActive) Color(0xFF334735) else FugaPanel)
                        .clickable { filter = key }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        color = if (isActive) FugaGreen else FugaMuted,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }

        // Transactions List
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(17.dp))
                .background(Color(0xFF181D23))
                .border(1.dp, Color(0xFF2B333A), RoundedCornerShape(17.dp))
        ) {
            Column {
                if (filtered.isEmpty()) {
                    Text(
                        text = "Sin resultados para tu búsqueda.",
                        color = FugaMuted,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                } else {
                    filtered.forEach { item ->
                        TransactionItem(
                            item = item,
                            canDelete = true,
                            onDelete = { transactionToDelete = item }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "${filtered.size} registro(s) visible(s). Para eliminar, utiliza el botón ×.",
            color = Color(0xFF83929C),
            fontSize = 10.sp
        )
        Spacer(modifier = Modifier.height(20.dp))
    }
}
