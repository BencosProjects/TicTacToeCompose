package org.example.tictactoe.screens.code

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.example.tictactoe.NavData.gamePrefs
import org.example.tictactoe.NavData.navigationOps

@Composable
fun HomeScreen(navController: NavController){
    // עוטף את כל ה-UI ב-MaterialTheme 3
    MaterialTheme { // ודא ש-MaterialTheme מגיע מ-androidx.compose.material3
        Surface(
            modifier = Modifier.fillMaxSize(), // מילוי כל המסך
            color = MaterialTheme.colorScheme.background // צבע רקע מה-Theme
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
//                    .padding(16.dp), // ריפוד כללי למסך

            ) {
                // שימוש ב-Card לבלוק הכותרת למראה מודרני ונקי יותר
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.17f)
                        .padding(bottom = 35.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer // צבע רקע של הכרטיס
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp) // צל לכרטיס
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Game mode:",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.headlineMedium, // שימוש בסגנון טיפוגרפיה מובנה של MD3
                            color = MaterialTheme.colorScheme.onPrimaryContainer // צבע טקסט תואם לרקע הכרטיס
                        )
                    }
                }

        }
            // בלוק הכפתורים
            SetButtons { mode ->
             navController.navigate("${navigationOps.gamePage}/$mode")
            }
        }
    }

}


@Composable
fun ButtonMode(textMode : String, startGame : (mode : String) -> Unit) {
    Button(
        onClick = { startGame(textMode) },
        modifier = Modifier
            .fillMaxWidth(0.7f) // כפתורים יהיו 70% מרוחב המסך
            .padding(vertical = 8.dp) // רווח אנכי בין כפתורים
    ) {
        Text(
            textMode,
            style = MaterialTheme.typography.titleLarge // סגנון טקסט לכפתור
        )
    }
}

@Composable
fun SetButtons(startGame : (mode : String) -> Unit) {
    val list = listOf(
        gamePrefs.oneVoneMode,
        gamePrefs.easyMode,
        gamePrefs.mediumMode,
        gamePrefs.hardMode,
        gamePrefs.impossibleMode
    )

    Column( modifier = Modifier.fillMaxWidth(), // העמודה תמלא את רוחב ההורה
        verticalArrangement = Arrangement.Center, // רווח קבוע בין כל הכפתורים
        horizontalAlignment = Alignment.CenterHorizontally) {
        for (a in list) {
            ButtonMode(a, startGame)
        }
    }
}



