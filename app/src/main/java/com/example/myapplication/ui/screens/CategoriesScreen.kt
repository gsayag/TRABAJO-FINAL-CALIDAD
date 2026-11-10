package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.model.ExpenseCategory
import com.example.myapplication.model.Transaction
import com.example.myapplication.model.TransactionType
import com.example.myapplication.ui.components.formatCurrency
import com.example.myapplication.ui.theme.FugaBorder
import com.example.myapplication.ui.theme.FugaGreen
import com.example.myapplication.ui.theme.FugaMuted
import com.example.myapplication.ui.theme.FugaNoticeBg
import com.example.myapplication.ui.theme.FugaNoticeBorder
import com.example.myapplication.ui.theme.FugaText
import kotlin.math.roundToInt

data class CategoryStat(
    val category: ExpenseCategory,
    val value: Double
)

@Composable
fun CategoriesScreen(
    transactions: List<Transaction>
) {
    val thisMonthExpenses = remember(transactions) {
        transactions.filter { it.date.startsWith("2026-11") && it.type == TransactionType.EXPENSE }
    }
    val totalExpenses = remember(thisMonthExpenses) {
        thisMonthExpenses.sumOf { it.amount }
    }

    val byCategory = remember(thisMonthExpenses) {
        ExpenseCategory.entries.map { cat ->
            val sum = thisMonthExpenses
                .filter { it.category.equals(cat.displayName, ignoreCase = true) }
                .sumOf { it.amount }
            CategoryStat(cat, sum)
        }.filter { it.value > 0 }.sortedByDescending { it.value }
    }

    val topCategory = byCategory.firstOrNull()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        // Heading
        Text(
            text = "ANALIZA TUS HÁBITOS",
            color = FugaGreen,
            fontSize = 10.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.5.sp,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        Text(
            text = "¿En qué gastas?",
            color = FugaText,
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        Text(
            text = "Distribución por categorías durante noviembre de 2026.",
            color = FugaMuted,
            fontSize = 12.sp,
            lineHeight = 19.sp,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        // Main Category Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFF191F25))
                .border(1.dp, FugaBorder, RoundedCornerShape(20.dp))
                .padding(22.dp)
        ) {
            Column {
                Text(
                    text = "GASTOS DEL MES",
                    color = FugaMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = formatCurrency(totalExpenses),
                    color = FugaText,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.padding(top = 6.dp)
                )

                Spacer(modifier = Modifier.height(22.dp))

                // Segmented Bar
                if (totalExpenses > 0 && byCategory.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(14.dp)
                            .clip(RoundedCornerShape(7.dp))
                    ) {
                        byCategory.forEach { stat ->
                            val weight = (stat.value / totalExpenses).toFloat().coerceAtLeast(0.01f)
                            Box(
                                modifier = Modifier
                                    .weight(weight)
                                    .fillMaxSize()
                                    .background(stat.category.color)
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(14.dp)
                            .clip(RoundedCornerShape(7.dp))
                            .background(Color(0xFF30383F))
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Breakdown list
                if (byCategory.isEmpty()) {
                    Text(
                        text = "Todavía no hay gastos registrados para este mes.",
                        color = Color(0xFFAAB5AC),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                } else {
                    byCategory.forEach { stat ->
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(stat.category.color)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = stat.category.displayName,
                                    color = FugaText,
                                    fontSize = 14.sp,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = formatCurrency(stat.value),
                                    color = FugaText,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 14.sp
                                )
                            }
                            HorizontalDivider(color = FugaBorder, thickness = 1.dp)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Insight Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(FugaNoticeBg)
                .border(1.dp, FugaNoticeBorder, RoundedCornerShape(16.dp))
                .padding(15.dp)
        ) {
            Row(verticalAlignment = Alignment.Top) {
                Text(
                    text = "◴",
                    color = FugaGreen,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(end = 12.dp)
                )
                Column(modifier = Modifier.weight(1f)) {
                    val percent = if (totalExpenses > 0 && topCategory != null) {
                        ((topCategory.value / totalExpenses) * 100).roundToInt()
                    } else 0

                    Text(
                        text = if (topCategory != null) "Tu principal categoría es ${topCategory.category.displayName}" else "Clasifica tus primeros gastos",
                        color = FugaText,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (topCategory != null) {
                            "Representa el $percent% de tus gastos del mes. La clasificación se actualiza al registrar o eliminar movimientos."
                        } else {
                            "Agrega un movimiento para ver la distribución."
                        },
                        color = Color(0xFFAAB5AC),
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}
