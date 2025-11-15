package pl.training.runkeeper

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val tag = MainActivity::class.qualifiedName

    private lateinit var buttons: Array<Button>
    private lateinit var statusText: TextView
    private val board = Array(9) { "" }
    private var currentPlayer = "X"
    private var gameOver = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        statusText = findViewById(R.id.status_text)

        buttons = arrayOf(
            findViewById(R.id.button0),
            findViewById(R.id.button1),
            findViewById(R.id.button2),
            findViewById(R.id.button3),
            findViewById(R.id.button4),
            findViewById(R.id.button5),
            findViewById(R.id.button6),
            findViewById(R.id.button7),
            findViewById(R.id.button8)
        )

        buttons.forEachIndexed { index, button ->
            button.setOnClickListener { onCellClick(index) }
        }

        findViewById<Button>(R.id.reset_button).setOnClickListener { resetGame() }
    }

    private fun onCellClick(index: Int) {
        if (gameOver || board[index].isNotEmpty()) {
            return
        }

        board[index] = currentPlayer
        buttons[index].text = currentPlayer

        if (checkWinner()) {
            statusText.text = "Player $currentPlayer Wins!"
            gameOver = true
            return
        }

        if (board.all { it.isNotEmpty() }) {
            statusText.text = "It's a Draw!"
            gameOver = true
            return
        }

        currentPlayer = if (currentPlayer == "X") "O" else "X"
        statusText.text = "Player $currentPlayer's Turn"
    }

    private fun checkWinner(): Boolean {
        val winPatterns = arrayOf(
            arrayOf(0, 1, 2), arrayOf(3, 4, 5), arrayOf(6, 7, 8), // rows
            arrayOf(0, 3, 6), arrayOf(1, 4, 7), arrayOf(2, 5, 8), // columns
            arrayOf(0, 4, 8), arrayOf(2, 4, 6)                     // diagonals
        )

        for (pattern in winPatterns) {
            if (board[pattern[0]].isNotEmpty() &&
                board[pattern[0]] == board[pattern[1]] &&
                board[pattern[1]] == board[pattern[2]]) {
                return true
            }
        }

        return false
    }

    private fun resetGame() {
        board.fill("")
        buttons.forEach { it.text = "" }
        currentPlayer = "X"
        gameOver = false
        statusText.text = "Player X's Turn"
    }

}