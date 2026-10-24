package com.example.week5.Soal2.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.week5.Soal2.Model.AnswerRecord
import com.example.week5.Soal2.Model.Question

enum class QuizState {
    START,
    PLAYING,
    RESULT
}

class QuizViewModel : ViewModel() {

    // Bank soal (minimal 5 pertanyaan)
    private val questionBank = listOf(
        Question(
            id = 1,
            text = "Ibu Kota Di Indonesia?",
            options = listOf("Surabaya", "Bandung", "Jakarta", "Medan"),
            correctAnswer = "Jakarta"
        ),
        Question(
            id = 2,
            text = "Apakah Mata Kuliah VP susah?",
            options = listOf("Sangat Sulit", "Sulit", "Biasa Aja", "Sangat Mudah"),
            correctAnswer = "Sangat Mudah"
        ),
        Question(
            id = 3,
            text = "Natal dirayakan pada bulan?",
            options = listOf("Agustus", "Oktober", "November", "Desember"),
            correctAnswer = "Desember"
        ),
        Question(
            id = 4,
            text = "Mata Kuliah VP pakai apa?",
            options = listOf("Word", "Android Studio", "Canva", "Excel"),
            correctAnswer = "Android Studio"
        ),
        Question(
            id = 5,
            text = "Ada berapa hari dalam seminggu?",
            options = listOf("5", "6", "7", "8"),
            correctAnswer = "7"
        )
    )

    var currentScreen by mutableStateOf(QuizState.START)
        private set

    // State untuk Start Screen (Bonus Poin)
    var questionCountInput by mutableStateOf("")
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    // State permainan kuis
    var activeQuestions = mutableStateListOf<Question>()
        private set

    var currentQuestionIndex by mutableIntStateOf(0)
        private set

    var selectedOption by mutableStateOf<String?>(null)
        private set

    val answerRecords = mutableStateListOf<AnswerRecord>()

    val score: Int
        get() = answerRecords.count { it.isCorrect }

    val totalPlayingQuestions: Int
        get() = activeQuestions.size

    val currentQuestion: Question?
        get() = activeQuestions.getOrNull(currentQuestionIndex)

    fun onQuestionCountChange(input: String) {
        questionCountInput = input
        if (errorMessage != null) {
            errorMessage = null
        }
    }

    // Memulai kuis dengan validasi jumlah soal
    fun startQuiz() {
        val count = questionCountInput.toIntOrNull()

        if (count == null || count < 1) {
            errorMessage = "Please enter a valid number (1 - ${questionBank.size})"
            return
        }

        if (count > questionBank.size) {
            errorMessage = "Maximum is ${questionBank.size} questions"
            return
        }

        errorMessage = null
        activeQuestions.clear()
        // Acak dan ambil sejumlah yang diminta
        activeQuestions.addAll(questionBank.shuffled().take(count))
        currentQuestionIndex = 0
        selectedOption = null
        answerRecords.clear()
        currentScreen = QuizState.PLAYING
    }

    fun selectOption(option: String) {
        selectedOption = option
    }

    fun submitAnswerAndNext() {
        val q = currentQuestion ?: return
        val chosen = selectedOption ?: return

        answerRecords.add(
            AnswerRecord(
                questionNumber = currentQuestionIndex + 1,
                questionText = q.text,
                selectedAnswer = chosen,
                correctAnswer = q.correctAnswer,
                isCorrect = chosen == q.correctAnswer
            )
        )

        if (currentQuestionIndex + 1 < activeQuestions.size) {
            currentQuestionIndex++
            selectedOption = null
        } else {
            currentScreen = QuizState.RESULT
        }
    }

    fun restartQuiz() {
        questionCountInput = ""
        errorMessage = null
        currentScreen = QuizState.START
    }
}