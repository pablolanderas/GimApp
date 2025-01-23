package com.example.gimapp.views.addTraining

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gimapp.ui.viewModels.TrainingViewModel
import com.example.gimapp.ui.views.components.Header
import com.example.gimapp.data.database.DataBase
import com.example.gimapp.data.database.daos.DaosDatabase_Impl
import com.example.gimapp.domain.Training
import com.example.gimapp.domain.TrainingExercise
import com.example.gimapp.ui.theme.GimAppTheme
import com.example.gimapp.ui.views.GimScreens
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
                val textStyle = MaterialTheme.typography.bodyMedium
                Text(
                    text = exercise.exercise.name.replaceFirstChar { it.uppercase() },
                    style = textStyle.copy(
                        color = Color.White,
                        textAlign = TextAlign.Left
                    ),
                    modifier = Modifier
                        .padding(10.dp)
                        .weight(1f)
                )
                Text(
                    text = "x${exercise.sets.size}",
                    style = textStyle.copy(
                        color = MaterialTheme.colorScheme.secondary,
                        textAlign = TextAlign.Right
                    ),
                    modifier = Modifier
                        .padding(10.dp)
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
val NULL_TRAIN = Training(LocalDate.of(2024, 8, 8), mutableListOf<TrainingExercise>(), null, false)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EndRoutine(
    viewModel: TrainingViewModel
) {
    val training: Training by viewModel.training.observeAsState(initial = NULL_TRAIN)
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
                style = MaterialTheme.typography.titleLarge.copy(
                    color = Color.White
                ),
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
                onClick = { viewModel.navigateTo(GimScreens.AddExerciseToTraining) },
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
                    style = MaterialTheme.typography.bodyLarge,
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
                onClick = { viewModel.tryToEndActualTraining() },
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
                    style = MaterialTheme.typography.bodyLarge.copy(
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
fun PreviewEndRoutine() {
    GimAppTheme {
        EndRoutine( viewModel = TrainingViewModel(DataBase(DaosDatabase_Impl())))
    }
}