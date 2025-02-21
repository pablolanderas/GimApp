package com.example.gimapp.ui.views.historical

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.gimapp.data.database.DataBase
import com.example.gimapp.data.database.daos.DaosDatabase_Impl
import com.example.gimapp.domain.Exercise
import com.example.gimapp.domain.ExerciseSet
import com.example.gimapp.domain.MuscularGroup
import com.example.gimapp.domain.Training
import com.example.gimapp.domain.TrainingExercise
import com.example.gimapp.ui.theme.GimAppTheme
import com.example.gimapp.ui.viewModels.HistoricalViewModel
import com.example.gimapp.ui.views.components.Header
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@SuppressLint("DefaultLocale")
@Composable
fun ExerciseItem(
    exercise: TrainingExercise,
    modifier: Modifier = Modifier
) {
    Box(
        modifier
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(15.dp)
            )
    ) {
        Column(
            Modifier
                .padding(20.dp)
        ){

            var rowSize by remember { mutableStateOf(IntSize.Zero) }
            val density = LocalDensity.current

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .onSizeChanged { size -> rowSize = size }
            ){
                Text(
                    text = exercise.exercise.name.replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White,
                        textAlign = TextAlign.Left
                    ),
                    modifier = Modifier
                        .widthIn(max = with(density) { (rowSize.width * 0.55f).toDp() })
                        .wrapContentWidth()
                )
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .weight(maxOf(1f, 35.dp.value))
                        .padding(horizontal = 15.dp)
                )
                Text(
                    text = exercise.exercise.mode.replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.secondary,
                        textAlign = TextAlign.Right
                    )
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
            exercise.sets.forEach { set ->
                Text(
                    text = "${if (set.weight % 1.0 == 0.0) set.weight.toInt() else String.format("%.1f", set.weight)}kg x ${set.reps}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.secondary
                    )
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SeeTraining(viewModel: HistoricalViewModel) {
    
    val training by viewModel.selectedTraining.observeAsState(initial = Training(LocalDate.of(2025, 1, 1), mutableListOf(), null, true))
    
    Header(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = training.date.format(
                DateTimeFormatter.ofPattern(
                    "d 'de' MMMM 'de' yyyy",
                    Locale("es", "ES")
                )
            ),
            style = MaterialTheme.typography.titleLarge.copy(
                color = Color.White
            ),
            modifier = Modifier
                .padding(top = 20.dp)
        )
        Column(
            modifier = Modifier
                .padding(25.dp)
                .fillMaxWidth()
                .weight(1f)
                .background(
                    color = MaterialTheme.colorScheme.secondary,
                    shape = RoundedCornerShape(20.dp)
                )
        ){
            Text(
                text = (
                    training.routine?.name?.replaceFirstChar { it.uppercase() }
                        ?: "Entrenamiento sin rutina"
                    ) + if (training.modifiedRoutine) " (Modificada)" else "",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.White,
                    textAlign = TextAlign.Left
                ),
                modifier = Modifier
                    .padding(20.dp)
            )
            LazyColumn {
                items(training.exercises) {
                    ExerciseItem(
                        it,
                        Modifier
                            .padding(
                                horizontal = 16.dp,
                                vertical = 8.dp
                            )
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showSystemUi = true)
@Composable
fun PreviewSeeTraining() {
    GimAppTheme {
        SeeTraining(
            HistoricalViewModel(DataBase(DaosDatabase_Impl()))
        )
    }
}