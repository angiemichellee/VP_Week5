package com.example.week5.Soal2.UI

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.week5.Soal2.ViewModel.QuizState
import com.example.week5.Soal2.ViewModel.QuizViewModel

val PrimaryDark = Color(0xFF4C5B82)
val SoftLavender = Color(0xFFE2E7F7)
val SoftCardBg = Color(0xFFF2F4FC)

@Composable
fun QuizApp(viewModel: QuizViewModel = viewModel()) {
    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            if (viewModel.currentScreen == QuizState.START) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color.White,
                        shadowElevation = 4.dp,
                        modifier = Modifier.size(52.dp)
                    ) {
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = Color.DarkGray
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (viewModel.currentScreen) {
                QuizState.START -> StartScreen(viewModel)
                QuizState.PLAYING -> QuestionScreen(viewModel)
                QuizState.RESULT -> ResultScreen(viewModel)
            }
        }
    }
}

// ================= 1. START SCREEN (BONUS) =================
@Composable
fun StartScreen(viewModel: QuizViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Mini Quiz",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "How many questions do you want? (1 - 5)",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = viewModel.questionCountInput,
            onValueChange = { viewModel.onQuestionCountChange(it) },
            label = { Text("Number of questions") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = viewModel.errorMessage != null,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(8.dp)
        )

        if (viewModel.errorMessage != null) {
            Text(
                text = viewModel.errorMessage ?: "",
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.startQuiz() },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SoftLavender,
                contentColor = Color.Gray
            )
        ) {
            Text(text = "Start Quiz", fontWeight = FontWeight.SemiBold)
        }
    }
}

// ================= 2. QUESTION SCREEN =================
@Composable
fun QuestionScreen(viewModel: QuizViewModel) {
    val q = viewModel.currentQuestion ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Question ${viewModel.currentQuestionIndex + 1}",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = q.text,
            fontSize = 15.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        q.options.forEach { option ->
            val isSelected = viewModel.selectedOption == option

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(
                        color = if (isSelected) PrimaryDark else SoftLavender,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .clickable { viewModel.selectOption(option) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = option,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isSelected) Color.White else Color.DarkGray
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        Spacer(modifier = Modifier.height(20.dp))

        val isAnswerChosen = viewModel.selectedOption != null
        Button(
            onClick = { viewModel.submitAnswerAndNext() },
            enabled = isAnswerChosen,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryDark,
                contentColor = Color.White,
                disabledContainerColor = SoftLavender.copy(alpha = 0.6f),
                disabledContentColor = Color.LightGray
            )
        ) {
            Text(text = "Next", fontWeight = FontWeight.SemiBold)
        }
    }
}

// ================= 3. RESULT SCREEN =================
@Composable
fun ResultScreen(viewModel: QuizViewModel) {
    val wrongAnswers = viewModel.answerRecords.filter { !it.isCorrect }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Quiz Finished!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Your Score: ${viewModel.score} / ${viewModel.totalPlayingQuestions}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (wrongAnswers.isEmpty()) {
            Text(
                text = "Perfect! No wrong answers.",
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(24.dp))
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(wrongAnswers) { record ->
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Q${record.questionNumber}: ${record.questionText}",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                        Text(
                            text = "Your Answer: ${record.selectedAnswer}",
                            fontSize = 13.sp,
                            color = Color(0xFFCC3333)
                        )
                        Text(
                            text = "Correct Answer: ${record.correctAnswer}",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Divider(color = Color.LightGray.copy(alpha = 0.5f), thickness = 0.8.dp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        Button(
            onClick = { viewModel.restartQuiz() },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryDark,
                contentColor = Color.White
            )
        ) {
            Text(text = "Restart Quiz", fontWeight = FontWeight.SemiBold)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ExpenseScreenPreview() {
    QuizApp()
}