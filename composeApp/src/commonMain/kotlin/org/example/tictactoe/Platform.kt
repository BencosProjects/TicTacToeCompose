package org.example.tictactoe

import androidx.compose.runtime.Composable

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun isAndroid() : Boolean

expect fun isDesktop() : Boolean

@Composable
expect fun WinnerDialog(winner: String, onDismiss: () -> Unit)
