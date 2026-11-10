package com.example.myapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.FugaBorder
import com.example.myapplication.ui.theme.FugaBottomBarBg
import com.example.myapplication.ui.theme.FugaGreen
import com.example.myapplication.ui.theme.FugaMuted

sealed class Screen(val route: String, val title: String, val symbol: String) {
    object Dashboard : Screen("dashboard", "Inicio", "▦")
    object History : Screen("history", "Actividad", "↔")
    object Add : Screen("add", "", "＋")
    object Categories : Screen("categories", "Categorías", "◴")
}

@Composable
fun BottomNavBar(
    currentScreen: Screen,
    onScreenSelected: (Screen) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(FugaBottomBarBg)
            .border(1.dp, FugaBorder)
            .border(width = 0.dp, color = Color.Transparent), // divider line on top handled by border
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val items = listOf(Screen.Dashboard, Screen.History, Screen.Add, Screen.Categories)

        items.forEach { item ->
            val isSelected = currentScreen == item

            if (item == Screen.Add) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF304833))
                        .clickable { onScreenSelected(item) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.symbol,
                        color = FugaGreen,
                        fontSize = 27.sp
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .clickable { onScreenSelected(item) },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = item.symbol,
                        color = if (isSelected) FugaGreen else FugaMuted,
                        fontSize = 24.sp
                    )
                    Text(
                        text = item.title,
                        color = if (isSelected) FugaGreen else FugaMuted,
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}
