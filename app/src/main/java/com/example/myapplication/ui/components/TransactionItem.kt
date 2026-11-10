package com.example.myapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.model.Transaction
import com.example.myapplication.model.TransactionType
import com.example.myapplication.ui.theme.FugaBorder
import com.example.myapplication.ui.theme.FugaGreen
import com.example.myapplication.ui.theme.FugaMuted
import com.example.myapplication.ui.theme.FugaPanel2
import com.example.myapplication.ui.theme.FugaText
import java.util.Locale

fun formatCurrency(amount: Double): String {
    return String.format(Locale.US, "S/ %.2f", amount)
}

@Composable
fun TransactionItem(
    item: Transaction,
    canDelete: Boolean = false,
    onDelete: (() -> Unit)? = null
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(13.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(FugaPanel2),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item.symbol,
                    color = item.color,
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.description,
                    color = FugaText,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${item.category} · ${item.date}",
                    color = FugaMuted,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "${if (item.type == TransactionType.INCOME) "+" else "−"} ${formatCurrency(item.amount)}",
                color = if (item.type == TransactionType.INCOME) FugaGreen else FugaText,
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold
            )

            if (canDelete && onDelete != null) {
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .clickable { onDelete() }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "×",
                        color = Color(0xFF9BA6B1),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        HorizontalDivider(color = FugaBorder, thickness = 1.dp)
    }
}
