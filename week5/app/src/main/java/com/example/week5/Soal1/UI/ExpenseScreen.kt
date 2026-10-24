package com.example.week5.Soal1.UI

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.week5.Soal1.Model.Expense
import com.example.week5.Soal1.ViewModel.ExpenseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseScreen(viewModel: ExpenseViewModel = viewModel()) {
    val lavenderBg = Color(0xFFECEEFB)

    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    shadowElevation = 4.dp,
                    modifier = Modifier.size(52.dp)
                ) {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = Color.DarkGray
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Expenses",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Total Expense : ${viewModel.formatRupiah(viewModel.totalExpense)}",
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = viewModel.expenseNameInput,
                onValueChange = { viewModel.onExpenseNameChange(it) },
                label = { Text("Expense Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = viewModel.amountInput,
                    onValueChange = { viewModel.onAmountChange(it) },
                    label = { Text("Amount of Expense") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = viewModel.errorMessage != null,
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                FilledTonalIconButton(
                    onClick = { viewModel.addExpense() },
                    modifier = Modifier.size(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = lavenderBg
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Expense",
                        tint = Color.Gray
                    )
                }
            }

            if (viewModel.errorMessage != null) {
                Text(
                    text = viewModel.errorMessage ?: "",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(viewModel.expenseList, key = { it.id }) { item ->
                    _root_ide_package_.com.example.week5.Soal1.UI.ExpenseCard(
                        expense = item,
                        formattedAmount = viewModel.formatRupiah(item.amount),
                        onEdit = { viewModel.openEditDialog(item) },
                        onDelete = { viewModel.deleteExpense(item) }
                    )
                }
            }
        }
    }

    if (viewModel.selectedExpenseForEdit != null) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissEditDialog() },
            title = { Text(text = "Edit Amount") },
            text = {
                Column {
                    Text(
                        text = "Expense: ${viewModel.selectedExpenseForEdit?.name}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = viewModel.editAmountInput,
                        onValueChange = { viewModel.onEditAmountChange(it) },
                        label = { Text("New Amount") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { viewModel.saveEditedAmount() }) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissEditDialog() }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun ExpenseCard(
    expense: Expense,
    formattedAmount: String,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFECEEFB)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Name : ${expense.name}",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = formattedAmount,
                    fontSize = 13.sp,
                    color = Color.DarkGray
                )
            }

            Row {
                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Amount",
                        tint = Color(0xFFD4A017),
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Expense",
                        tint = Color(0xFFCC3333),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ExpenseScreenPreview() {
    _root_ide_package_.com.example.week5.Soal1.UI.ExpenseScreen()
}