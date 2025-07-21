package org.example.tictactoe.screens.code

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import org.example.tictactoe.NavData.gamePrefs
import org.example.tictactoe.WinnerDialog
import kotlin.time.Duration
import kotlin.time.TimeSource


@Composable
fun GameScreen(navController: NavController, mode: String?, viewModel: GameViewModel){
    val boardTexts = remember { mutableStateMapOf<Pair<Int, Int>, String>() }
    var currentPlayer by remember { mutableStateOf("X") }
    val gameMode : String = mode + ""
    var botCLick by remember { mutableStateOf <Pair<Int, Int>?>(null) }
    var winningCells by remember { mutableStateOf<List<Pair<Int, Int>>?>(null) }
    var winner by remember { mutableStateOf<String?>(null) }
    val showDialog by viewModel.showDialog.collectAsState()

    if (showDialog && winner != null) {
        WinnerDialog(
            winner = winner!!,
            onDismiss = {
                viewModel.dismissDialog()
                navController.navigateUp()
            }
        )
    }

    LaunchedEffect(Unit){
        for (row in 0 until 3){
            for (column in 0 until 3){
                boardTexts[Pair(row, column)] = ""
            }
        }
    }

    MaterialTheme{
        Surface(Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                    , verticalArrangement = Arrangement.Center) {
                    XOTable(
                        onCellClick = { row, col ->
                            boardTexts[Pair(row, col)] = currentPlayer
                            if (winner == null) { // ודא שאין מנצח ושהתא ריק
                                boardTexts[Pair(row, col)] = currentPlayer
                                val winningCombination = checkForWinner(boardTexts)
                                if (winningCombination != null) {
                                    winningCells = winningCombination // שמור את קו הניצחון במצב
                                    winner = currentPlayer
                                    viewModel.showDialogDialog(winner!!)
                                    return@XOTable
                                }
                            }
                            if (gameMode.equals(gamePrefs.oneVoneMode)){
                                currentPlayer = if (currentPlayer.equals("X")) "O" else "X"
                            }else if (getEmptyButtons(boardTexts).isNotEmpty()){
                                val pos : Pair<Int,Int>
                                when (gameMode){
                                    gamePrefs.easyMode -> pos = easyMode(boardTexts)
                                    gamePrefs.mediumMode -> pos = mediumMode(boardTexts)
                                    gamePrefs.hardMode -> pos = hardMode(boardTexts)
                                    gamePrefs.impossibleMode -> pos = impossibleMode(boardTexts)
                                    else -> pos = easyMode(boardTexts)
                                }
                                botCLick = pos
                                boardTexts[pos] = "O"

                                if (winner == null) { // ודא שאין מנצח ושהתא ריק
                                    boardTexts[Pair(row, col)] = currentPlayer
                                    val winningCombination = checkForWinner(boardTexts)
                                    if (winningCombination != null) {
                                        winningCells = winningCombination // שמור את קו הניצחון במצב
                                        winner = "O"
                                        viewModel.showDialogDialog(winner!!)
                                    }
                                }
                            }
                            if (getEmptyButtons(boardTexts).isEmpty()){
                                winner = ""
                                viewModel.showDialogDialog("")
                            }
                        },
                        cellTexts = boardTexts,
                        posBotClick = botCLick,
                        winningCells = winningCells
                    )
                }
            }
        }
    }
}



// פונקציית בדיקת הניצחון צריכה להשתנות כדי להחזיר את הקו המנצח
fun checkForWinner(board: Map<Pair<Int, Int>, String>): List<Pair<Int, Int>>? {
    val winningCombinations = listOf(
        // שורות
        listOf(Pair(0, 0), Pair(0, 1), Pair(0, 2)),
        listOf(Pair(1, 0), Pair(1, 1), Pair(1, 2)),
        listOf(Pair(2, 0), Pair(2, 1), Pair(2, 2)),
        // עמודות
        listOf(Pair(0, 0), Pair(1, 0), Pair(2, 0)),
        listOf(Pair(0, 1), Pair(1, 1), Pair(2, 1)),
        listOf(Pair(0, 2), Pair(1, 2), Pair(2, 2)),
        // אלכסונים
        listOf(Pair(0, 0), Pair(1, 1), Pair(2, 2)),
        listOf(Pair(0, 2), Pair(1, 1), Pair(2, 0))
    )

    for (combination in winningCombinations) {
        val (c1, c2, c3) = combination
        if (board[c1]?.isNotEmpty() == true && board[c1] == board[c2] && board[c1] == board[c3]) {
            return combination // החזר את הקומבינציה המנצחת
        }
    }
    return null // אין מנצח
}



@Composable
private fun Modifier.pulsatingScaleModifier(
    isTriggered: Boolean,
): Modifier {
    var triggerPulseInternal by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (triggerPulseInternal) 1.1f else 1f,
        animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing),
        label = "button_pulse_scale"
    )

    // LaunchedEffect שמגיב לטריגר החיצוני
    LaunchedEffect(isTriggered) {
        if (isTriggered) {
            triggerPulseInternal = true // התחל את הפעימה החוצה
            delay(400) // המתן לפעימה החוצה
            triggerPulseInternal = false // התחל את הפעימה פנימה
            delay(400) // המתן לפעימה פנימה
        }
    }

    // זהו ה-Modifier שמופעל על הכפתור
    return this.scale(scale)
}

@Composable
fun GameButton(
    onClick: () -> Unit
    , text: String,
    isBotClicked: Boolean,
    isWinningCell: Boolean
){
    var triggerPulse by remember { mutableStateOf(false) }
    // הערך המונפש של פקטור הגודל (scale)
    var isClickable by remember { mutableStateOf(true) }

    // השתמש במצב החדש כדי לשנות את מראה הטקסט
    val textColor = if (isWinningCell) {
        Color.Green // או כל צבע אחר שתבחר להדגשה
    } else {
        MaterialTheme.colorScheme.onPrimaryContainer
    }

    LaunchedEffect(isBotClicked){
        if (!triggerPulse && isBotClicked) {
            isClickable = false
            triggerPulse = true
        }
    }

    Button(onClick = {
        if (!triggerPulse) {
            isClickable = false
            triggerPulse = true
            onClick()
        }
    },
        modifier = Modifier
            .size(100.dp)
            .padding(5.dp)
            .pulsatingScaleModifier(
                isTriggered = triggerPulse,
            ),
        shape = RectangleShape,
        enabled = isClickable,
        ){
        Text(text,
            style = MaterialTheme.typography.displayMedium,
            color = textColor)
    }
}

@Composable
fun RowButtons(
    row: Int,
    onCellClick: (row: Int, col: Int) -> Unit,
    cellTexts: SnapshotStateMap<Pair<Int, Int>, String>,
    posBotClick: Pair<Int, Int>?,
    winningCells: List<Pair<Int, Int>>?
){
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        for (col in 0 until 3){
            val cellText = cellTexts[Pair(row, col)] ?: ""
            val isBotClicked = posBotClick == Pair(row, col)
            val isWinningCell = winningCells?.contains(Pair(row, col)) ?: false
            GameButton(onClick = {
                onCellClick(row, col)
            },
                text = cellText,
                isBotClicked = isBotClicked,
                isWinningCell = isWinningCell
            )
        }
    }
}

@Composable
fun XOTable(
    onCellClick: (row: Int, col: Int) -> Unit, // Callback עבור לחיצה על תא ספציפי
    cellTexts: SnapshotStateMap<Pair<Int, Int>, String>, // מצב הטקסטים עבור כל תא){
    posBotClick: Pair<Int, Int>?,
    winningCells: List<Pair<Int, Int>>?
){
    for (row in 0 until 3) { // 0, 1, 2
        RowButtons(
            row = row, // מעבירים את מספר השורה
            onCellClick = onCellClick, // מעבירים את ה-Callback
            cellTexts = cellTexts, // מעבירים את מצב הטקסטים
            posBotClick = posBotClick,
            winningCells = winningCells
        )
    }
}
