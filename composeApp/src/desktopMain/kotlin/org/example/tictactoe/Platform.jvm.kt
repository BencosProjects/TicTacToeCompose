package org.example.tictactoe

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogWindow
import androidx.compose.ui.window.rememberDialogState

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()

actual fun isAndroid(): Boolean = false
actual fun isDesktop(): Boolean = true

@Composable
actual fun WinnerDialog(winner: String, onDismiss: () -> Unit) {

    val text : String = if (winner.isNotEmpty()) "well $winner is the winner!" else "well it's a tie!"


    DialogWindow(
        onCloseRequest = onDismiss,
        state = rememberDialogState(),
        title = "game over!",
    ) {
        // Box כמְכל במקום Column
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center // ממקם את כל התוכן במרכז
        ) {
            // Box פנימי נוסף כדי לסדר אנכית, מכיוון ש-Box לא מסדר אוטומטית אנכית
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.TopCenter
            ) {
                Text(
                    text = text,
                    textAlign = TextAlign.Center
                )
            }

            // Box נוסף כדי למקם את הכפתור בתחתית המרכז
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(top = 16.dp)
            ) {
                Button(
                    onClick = onDismiss
                ) {
                    Text("סגור")
                }
            }
        }
    }
}