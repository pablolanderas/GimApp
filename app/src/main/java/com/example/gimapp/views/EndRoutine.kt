package com.example.gimapp.views

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.gimapp.R
import com.example.gimapp.components.Header
import com.example.gimapp.domain.Exercise
import com.example.gimapp.domain.ExerciseSet
import com.example.gimapp.domain.Training
import com.example.gimapp.domain.TrainingExercise
import com.example.gimapp.ui.theme.GimAppTheme
import java.time.LocalDate

@Composable
fun ExercisesViwer(
    exercises: List<TrainingExercise>,
    modifier: Modifier = Modifier
) {
    LazyColumn (
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(20.dp)
            )
    ) {
        items(exercises) { exercise ->
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 5.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                Text(
                    text = exercise.exercise.name.replaceFirstChar { it.uppercase() },
                    style = TextStyle(
                        color = Color.White,
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize
                    ),
                    modifier = Modifier
                        .padding(10.dp)
                        .weight(1f)
                )
                Text(
                    text = "x${exercise.sets.size}",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize
                    ),
                    modifier = Modifier
                        .padding(10.dp)
                )
            }
        }
    }
}

@Composable
fun EndRoutine(
    training: Training,
    onExtraExercise: () -> Unit,
    onEndTraining: () -> Unit
) {
    Header(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Text(
                text = "Ejercicios terminados",
                style = TextStyle(
                    color = Color.White,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)
                    .align(Alignment.Center)
            )
        }
        ExercisesViwer(
            exercises = training.exercises,
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp)
                .weight(2f)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Button(
                onClick = onExtraExercise,
                modifier = Modifier
                    .align(Alignment.Center),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "Ejercicio extra",
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize
                    ),
                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp)
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Button(
                onClick = onEndTraining,
                modifier = Modifier
                    .align(Alignment.Center),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "Acabar\nentrenamiento",
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp)
                )
            }
        }
        Spacer(modifier = Modifier.weight(0.2f))
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showSystemUi = true)
@Composable
fun PrevieEndRoutine() {
    GimAppTheme {
        EndRoutine(training =
            Training(LocalDate.now(),
                mutableListOf(
                    TrainingExercise(
                        exercise = Exercise("press banca", "normal", R.drawable.press_banca),
                        date = LocalDate.of(2024, 8, 13),
                        sets = mutableListOf(
                            ExerciseSet(weight = 70.0, reps = 10, effort = 2),
                            ExerciseSet(weight = 70.0, reps = 10, effort = 2),
                            ExerciseSet(weight = 70.0, reps = 10, effort = 2),
                            ExerciseSet(weight = 70.0, reps = 10, effort = 2)
                        )
                    ),
                    TrainingExercise(
                        exercise = Exercise("press inclinado", "normal", R.drawable.press_banca),
                        date = LocalDate.of(2024, 8, 13),
                        sets = mutableListOf(
                            ExerciseSet(weight = 70.0, reps = 10, effort = 2),
                            ExerciseSet(weight = 70.0, reps = 10, effort = 2),
                            ExerciseSet(weight = 70.0, reps = 10, effort = 2),
                            ExerciseSet(weight = 70.0, reps = 10, effort = 2)
                        )
                    )
                )
            ),
            onEndTraining = {},
            onExtraExercise = {}
        )
    }
}