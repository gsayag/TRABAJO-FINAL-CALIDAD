package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.model.ExpenseCategory
import com.example.myapplication.model.TransactionType
import com.example.myapplication.ui.theme.FugaBorder
import com.example.myapplication.ui.theme.FugaGreen
import com.example.myapplication.ui.theme.FugaMuted
import com.example.myapplication.ui.theme.FugaPanel
import com.example.myapplication.ui.theme.FugaText
import com.example.myapplication.ui.theme.FugaWarning
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddTransactionScreen(
    onAddTransaction: (TransactionType, Double, String, String, String) -> Unit
) {
    var type by remember { mutableStateOf(TransactionType.EXPENSE) }
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Alimentación") }
    var date by remember { mutableStateOf("2026-11-10") }
    var formError by remember { mutableStateOf("") }

    val expenseCategoryNames = remember { ExpenseCategory.entries.map { it.displayName } }

    fun handleSave() {
        val cleanAmount = amount.replace(',', '.')
        val valDouble = cleanAmount.toDoubleOrNull()
        if (valDouble == null || valDouble <= 0 || valDouble > 100000000) {
            formError = "Ingresa un importe válido mayor que cero."
            return
        }
        if (description.trim().length < 3) {
            formError = "La descripción debe tener 3 caracteres como mínimo."
            return
        }
        val datePattern = Regex("""^\d{4}-\d{2}-\d{2}$""")
        if (!datePattern.matches(date)) {
            formError = "Fecha inválida: usa AAAA-MM-DD."
            return
        }
        try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            sdf.isLenient = false
            sdf.parse(date)
        } catch (e: Exception) {
            formError = "Fecha inválida: usa AAAA-MM-DD."
            return
        }

        formError = ""
        val finalCategory = if (type == TransactionType.INCOME) "Ingresos" else category
        onAddTransaction(type, (valDouble * 100).toLong() / 100.0, finalCategory, description.trim(), date)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        // Heading
        Text(
            text = "TU DINERO, BAJO CONTROL",
            color = FugaGreen,
            fontSize = 10.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.5.sp,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        Text(
            text = "Nuevo movimiento",
            color = FugaText,
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        Text(
            text = "Registra un gasto o un ingreso en pocos segundos.",
            color = FugaMuted,
            fontSize = 12.sp,
            lineHeight = 19.sp,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        // Form Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(FugaPanel)
                .border(1.dp, FugaBorder, RoundedCornerShape(20.dp))
                .padding(18.dp)
        ) {
            Column {
                // Type Toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF11161B))
                        .padding(5.dp),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(9.dp))
                            .background(if (type == TransactionType.EXPENSE) Color(0xFF334735) else Color.Transparent)
                            .clickable {
                                type = TransactionType.EXPENSE
                                category = "Alimentación"
                            }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "↗ Gasto",
                            color = if (type == TransactionType.EXPENSE) FugaGreen else FugaMuted,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(9.dp))
                            .background(if (type == TransactionType.INCOME) Color(0xFF334735) else Color.Transparent)
                            .clickable {
                                type = TransactionType.INCOME
                                category = "Ingresos"
                            }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "↘ Ingreso",
                            color = if (type == TransactionType.INCOME) FugaGreen else FugaMuted,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Amount Field
                Text(
                    text = "Importe (PEN)",
                    color = Color(0xFFB4BEC7),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 9.dp)
                )
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    placeholder = { Text("0.00", color = Color(0xFF83909C), fontSize = 24.sp, fontWeight = FontWeight.ExtraBold) },
                    singleLine = true,
                    textStyle = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = FugaGreen),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = FugaPanel,
                        unfocusedContainerColor = FugaPanel,
                        focusedBorderColor = FugaGreen,
                        unfocusedBorderColor = Color(0xFF3D4650),
                        focusedTextColor = FugaGreen,
                        unfocusedTextColor = FugaGreen
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Description Field
                Text(
                    text = "Descripción",
                    color = Color(0xFFB4BEC7),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 9.dp)
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { if (it.length <= 90) description = it },
                    placeholder = { Text("Ej. Almuerzo, sueldo...", color = Color(0xFF83909C), fontSize = 14.sp) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
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

                Spacer(modifier = Modifier.height(18.dp))

                // Category Selector
                Text(
                    text = "Categoría",
                    color = Color(0xFFB4BEC7),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 9.dp)
                )

                val currentOptions = if (type == TransactionType.INCOME) listOf("Ingresos") else expenseCategoryNames
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    currentOptions.forEach { cat ->
                        val isSelected = category == cat
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) Color(0xFF334735) else Color(0xFF252C34))
                                .border(
                                    1.dp,
                                    if (isSelected) Color(0xFF56794D) else Color.Transparent,
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable { category = cat }
                                .padding(horizontal = 12.dp, vertical = 9.dp)
                        ) {
                            Text(
                                text = cat,
                                color = if (isSelected) FugaGreen else FugaMuted,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Date Field
                Text(
                    text = "Fecha (AAAA-MM-DD)",
                    color = Color(0xFFB4BEC7),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 9.dp)
                )
                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    placeholder = { Text("2026-11-10", color = Color(0xFF83909C), fontSize = 14.sp) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
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

                if (formError.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(13.dp))
                    Text(
                        text = formError,
                        color = FugaWarning,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(25.dp))

                // Submit Button
                Button(
                    onClick = { handleSave() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = FugaGreen,
                        contentColor = Color(0xFF1B2C1B)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Guardar movimiento →",
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}
