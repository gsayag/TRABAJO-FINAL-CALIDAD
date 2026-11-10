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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.myapplication.model.Transaction
import com.example.myapplication.model.TransactionType
import com.example.myapplication.ui.components.TransactionItem
import com.example.myapplication.ui.components.formatCurrency
import com.example.myapplication.ui.theme.FugaBorder
import com.example.myapplication.ui.theme.FugaExpenseOrange
import com.example.myapplication.ui.theme.FugaGreen
import com.example.myapplication.ui.theme.FugaGreenBorder
import com.example.myapplication.ui.theme.FugaGreenDark
import com.example.myapplication.ui.theme.FugaMuted
import com.example.myapplication.ui.theme.FugaNoticeBg
import com.example.myapplication.ui.theme.FugaNoticeBorder
import com.example.myapplication.ui.theme.FugaPanel
import com.example.myapplication.ui.theme.FugaText

@Composable
fun DashboardScreen(
    transactions: List<Transaction>,
    onNavigateToHistory: () -> Unit,
    onNavigateToAdd: () -> Unit
) {
    val thisMonthTxs = remember(transactions) {
        transactions.filter { it.date.startsWith("2026-11") }
    }
    val income = remember(thisMonthTxs) {
        thisMonthTxs.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
    }
    val expenses = remember(thisMonthTxs) {
        thisMonthTxs.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
    }
    val balance = remember(transactions) {
        transactions.sumOf { if (it.type == TransactionType.INCOME) it.amount else -it.amount }
    }
    val recentTxs = remember(transactions) {
        transactions.sortedWith(compareByDescending<Transaction> { it.date }.thenByDescending { it.id }).take(4)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        // Heading
        Text(
            text = "TU PANORAMA FINANCIERO",
            color = FugaGreen,
            fontSize = 10.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.5.sp,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        Text(
            text = "Hola, bienvenido 👋",
            color = FugaText,
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        Text(
            text = "Toma el control de cada sol. Tus cifras, sin complicaciones.",
            color = FugaMuted,
            fontSize = 12.sp,
            lineHeight = 19.sp,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        // Hero Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(21.dp))
                .background(FugaGreenDark)
                .border(1.dp, FugaGreenBorder, RoundedCornerShape(21.dp))
                .padding(22.dp)
        ) {
            Column {
                Text(
                    text = "Saldo disponible · registros de demostración",
                    color = Color(0xFFBACCC0),
                    fontSize = 11.sp
                )
                Text(
                    text = formatCurrency(balance),
                    color = FugaText,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "↘ Ingresos del mes",
                            color = Color(0xFFBACCC0),
                            fontSize = 11.sp
                        )
                        Text(
                            text = formatCurrency(income),
                            color = FugaText,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "↗ Gastos del mes",
                            color = Color(0xFFBACCC0),
                            fontSize = 11.sp
                        )
                        Text(
                            text = formatCurrency(expenses),
                            color = FugaExpenseOrange,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(FugaGreen)
                            .clickable { onNavigateToAdd() }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "＋ Añadir",
                            color = Color(0xFF1B2C1B),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Stats Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(FugaPanel)
                    .border(1.dp, FugaBorder, RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Column {
                    Text(
                        text = "Movimientos del mes",
                        color = FugaMuted,
                        fontSize = 11.sp
                    )
                    Text(
                        text = "${thisMonthTxs.size}",
                        color = FugaText,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(FugaPanel)
                    .border(1.dp, FugaBorder, RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Column {
                    Text(
                        text = "Balance mensual",
                        color = FugaMuted,
                        fontSize = 11.sp
                    )
                    Text(
                        text = formatCurrency(income - expenses),
                        color = FugaGreen,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Section Title
        Text(
            text = "✧ Tu control comienza aquí",
            color = FugaText,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Notice Box
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
                    text = "✦",
                    color = FugaGreen,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(end = 12.dp)
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Revisa tus cobros periódicos",
                        color = FugaText,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Los movimientos se pueden registrar y clasificar. La detección automática de recurrencias y anomalías será parte de una siguiente etapa.",
                        color = Color(0xFFAAB5AC),
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Recent Movements Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Movimientos recientes",
                color = FugaText,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Ver todos →",
                color = FugaGreen,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onNavigateToHistory() }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Recent Movements List
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(17.dp))
                .background(Color(0xFF181D23))
                .border(1.dp, Color(0xFF2B333A), RoundedCornerShape(17.dp))
        ) {
            Column {
                if (recentTxs.isEmpty()) {
                    Text(
                        text = "Aún no registraste movimientos.",
                        color = FugaMuted,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                } else {
                    recentTxs.forEach { item ->
                        TransactionItem(item = item)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}
