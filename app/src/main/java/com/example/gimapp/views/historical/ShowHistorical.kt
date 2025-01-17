package com.example.gimapp.views.historical

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gimapp.components.Header
import com.example.gimapp.data.createSampleTraining
import com.example.gimapp.domain.Training
import com.example.gimapp.ui.theme.GimAppTheme
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun HistoricalElement(
    training: Training,
    onClickTraining: (Training) -> Unit
) {
    Box(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(15.dp)
            )
            .fillMaxWidth()
            .clickable { onClickTraining(training) }
    ) {
        Column (
            modifier = Modifier
                .padding(20.dp)
        ) {
            Text(
                text = training.date.format(
                    DateTimeFormatter.ofPattern(
                        "d 'de' MMMM 'de' yyyy",
                        Locale("es", "ES")
                    )
                ),
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.secondary
                )
            )
            Text(
                text = (
                        training.routine?.name?.replaceFirstChar { it.uppercase() } ?: "Entrenamiento sin rutina"
                        ) + if (training.modifiedRutine) " (Modificada)" else "",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.White
                ),
                modifier = Modifier
                    .padding(top = 20.dp, bottom = 15.dp)
            )
            training.exercises.forEach {
                Row(
                    modifier = Modifier
                        .padding(top = 5.dp)
                ){
                    Text(
                        text = it.exercise.name.replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.secondary,
                            textAlign = TextAlign.Left
                        ),
                        modifier = Modifier
                            .weight(1f)
                    )
                    Text(
                        text = "x${it.sets.size}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.White,
                            textAlign = TextAlign.Right
                        ),
                        modifier = Modifier
                    )
                }
            }
        }
    }
}

@Composable
fun HistoricalLayout(
    trainings: List<Training>,
    onClickTraining: (Training) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(trainings) { HistoricalElement(training = it, onClickTraining = onClickTraining) }
    }
}

@Composable
fun ShowHistorical(
    trainings: List<Training>,
    onClickTraining: (Training) -> Unit
){
    Header(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Historial",
            style = MaterialTheme.typography.titleLarge.copy(
                color = Color.White
            ),
            modifier = Modifier
                .padding(top=20.dp)
        )
        HistoricalLayout(
            trainings = trainings,
            onClickTraining = onClickTraining,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(20.dp)
                .padding(bottom = 40.dp)
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun PrevieShowHistorical() {
    GimAppTheme {
        ShowHistorical(
            listOf(
                createSampleTraining(),
                createSampleTraining(),
                createSampleTraining(),
                createSampleTraining()
            ), {}
        )
        //HistoricalElement(createSampleTraining())
    }
}