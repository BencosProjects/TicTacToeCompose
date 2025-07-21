package org.example.tictactoe

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.rememberDialogState
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.awt.Dialog

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "TicTacToe",
    ) {
        App()
    }
}
