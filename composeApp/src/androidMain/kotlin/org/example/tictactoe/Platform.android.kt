package org.example.tictactoe

import android.os.Build
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual fun isAndroid(): Boolean = true

actual fun isDesktop(): Boolean = false

@Composable
actual fun WinnerDialog(winner: String, onDismiss: () -> Unit) {

    val text : String = if (winner.isNotEmpty()) "well $winner is the winner!" else "well it's a tie!"

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("המשחק נגמר!") },
        text = { Text(text = text) },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("אישור")
            }
        }
    )
}