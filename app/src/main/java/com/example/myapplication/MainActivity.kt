package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.TransactionRepository
import com.example.myapplication.model.Transaction
import com.example.myapplication.ui.components.BottomNavBar
import com.example.myapplication.ui.components.Screen
import com.example.myapplication.ui.screens.AddTransactionScreen
import com.example.myapplication.ui.screens.CategoriesScreen
import com.example.myapplication.ui.screens.DashboardScreen
import com.example.myapplication.ui.screens.HistoryScreen
import com.example.myapplication.ui.theme.FugaBg
import com.example.myapplication.ui.theme.FugaGreen
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp() {
    val context = LocalContext.current
    val repository = remember { TransactionRepository(context) }
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Dashboard) }
    var transactions by remember { mutableStateOf<List<Transaction>>(repository.listTransactions()) }

    fun refreshData() {
        transactions = repository.listTransactions()
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(FugaBg),
        topBar = {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp)
                        .background(FugaBg)
                        .border(1.dp, Color(0xFF252B32))
                        .padding(horizontal = 22.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "◕ FUGA",
                        color = FugaGreen,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = "NOVIEMBRE 2026",
                        color = Color(0xFF879490),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(FugaGreen)
                    )
                }
            }
        },
        bottomBar = {
            BottomNavBar(
                currentScreen = currentScreen,
                onScreenSelected = { currentScreen = it }
            )
        },
        containerColor = FugaBg
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(FugaBg)
        ) {
            when (currentScreen) {
                is Screen.Dashboard -> {
                    DashboardScreen(
                        transactions = transactions,
                        onNavigateToHistory = { currentScreen = Screen.History },
                        onNavigateToAdd = { currentScreen = Screen.Add }
                    )
                }
                is Screen.History -> {
                    HistoryScreen(
                        transactions = transactions,
                        onDeleteTransaction = { id ->
                            repository.deleteTransaction(id)
                            refreshData()
                            Toast.makeText(context, "Movimiento eliminado", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
                is Screen.Add -> {
                    AddTransactionScreen(
                        onAddTransaction = { type, amount, category, description, date ->
                            repository.addTransaction(type, amount, category, description, date)
                            refreshData()
                            Toast.makeText(context, "Movimiento registrado correctamente", Toast.LENGTH_SHORT).show()
                            currentScreen = Screen.History
                        }
                    )
                }
                is Screen.Categories -> {
                    CategoriesScreen(
                        transactions = transactions
                    )
                }
            }
        }
    }
}
