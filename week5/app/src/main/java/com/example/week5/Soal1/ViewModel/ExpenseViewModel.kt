package com.example.week5.Soal1.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.week5.Soal1.Model.Expense
import java.text.NumberFormat
import java.util.Locale

class ExpenseViewModel : ViewModel() {

    val expenseList = mutableStateListOf<Expense>(
        Expense(name = "Makan Nasi", amount = 90000),
        Expense(name = "Investasi", amount = 10000000),
        Expense(name = "Kopi", amount = 18000)
    )

    var expenseNameInput by mutableStateOf("")
        private set

    var amountInput by mutableStateOf("")
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var selectedExpenseForEdit by mutableStateOf<Expense?>(null)
        private set

    var editAmountInput by mutableStateOf("")
        private set

    val totalExpense: Long
        get() = expenseList.sumOf { it.amount }

    fun onExpenseNameChange(newName: String) {
        expenseNameInput = newName
    }

    fun onAmountChange(newAmount: String) {
        amountInput = newAmount
        if (errorMessage != null) {
            errorMessage = null
        }
    }

    fun addExpense() {
        val amount = amountInput.toLongOrNull()

        if (expenseNameInput.isBlank()) {
            errorMessage = "Expense name cannot be empty"
            return
        }
        if (amount == null || amount <= 0) {
            errorMessage = "Amount must be greater than 0"
            return
        }

        expenseList.add(Expense(name = expenseNameInput.trim(), amount = amount))
        expenseNameInput = ""
        amountInput = ""
        errorMessage = null
    }

    fun deleteExpense(expense: Expense) {
        expenseList.remove(expense)
    }

    fun openEditDialog(expense: Expense) {
        selectedExpenseForEdit = expense
        editAmountInput = expense.amount.toString()
    }

    fun onEditAmountChange(newAmount: String) {
        editAmountInput = newAmount
    }

    fun saveEditedAmount() {
        val newAmount = editAmountInput.toLongOrNull()
        val current = selectedExpenseForEdit
        if (current != null && newAmount != null && newAmount > 0) {
            val index = expenseList.indexOfFirst { it.id == current.id }
            if (index != -1) {
                expenseList[index] = current.copy(amount = newAmount)
            }
        }
        selectedExpenseForEdit = null
        editAmountInput = ""
    }

    fun dismissEditDialog() {
        selectedExpenseForEdit = null
        editAmountInput = ""
    }

    fun formatRupiah(amount: Long): String {
        val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
        formatter.maximumFractionDigits = 0
        return formatter.format(amount).replace("Rp", "Rp ")
    }
}