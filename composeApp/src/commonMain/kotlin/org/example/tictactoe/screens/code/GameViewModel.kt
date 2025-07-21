package org.example.tictactoe.screens.code

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class GameViewModel {
    // מצב הדיאלוג
    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog

    // מצב המנצח (null אם אין מנצח עדיין)
    private val _winner = MutableStateFlow<String?>(null)
    val winner: StateFlow<String?> = _winner

    // פונקציה להצגת הדיאלוג עם המנצח
    fun showDialogDialog(winner: String) {
        _winner.value = winner
        _showDialog.value = true
    }

    // פונקציה לסגירת הדיאלוג
    fun dismissDialog() {
        _showDialog.value = false
        _winner.value = null // אפס את המנצח כשהדיאלוג נסגר
    }
}