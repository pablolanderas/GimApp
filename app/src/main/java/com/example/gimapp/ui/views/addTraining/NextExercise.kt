package com.example.gimapp.views.addTraining

import android.annotation.SuppressLint
import android.content.Context
import android.icu.text.DecimalFormat
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gimapp.R
import com.example.gimapp.ui.viewModels.TrainingViewModel
import com.example.gimapp.ui.views.components.Header
import com.example.gimapp.data.database.DataBase
import com.example.gimapp.data.database.daos.DaosDatabase_Impl
import com.example.gimapp.domain.ExerciseRoutine
import com.example.gimapp.domain.TrainingExercise
import com.example.gimapp.ui.theme.GimAppTheme
import com.example.gimapp.ui.viewModels.managers.NavigateManager
import com.example.gimapp.ui.views.GimScreens
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun InfoBar(
    title: String,
    titleStyle: TextStyle = MaterialTheme.typography.bodyMedium.copy(
        color = MaterialTheme.colorScheme.primary
    ),
    value: String,
    valueStyle: TextStyle = MaterialTheme.typography.bodyMedium.copy(
        color = Color.White
    ),
    modifier: Modifier,
    content: @Composable RowScope.() -> Unit = {}
) {
    Row(modifier = modifier){
        Text(
            text = title,
            style = titleStyle
        )
        Spacer(modifier = Modifier.weight(1F))
        content()
        Text(
            text = value,
            style = valueStyle
        )
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun ExerciseInfo(
    exerciseRoutine: ExerciseRoutine,
    weight: Double,
    addWeight: Boolean,
    removeWeight: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(15.dp)
            )
    ){
        Text(
            text = exerciseRoutine.exercise.name.replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.secondary,
                    shape = RoundedCornerShape(15.dp)
                )
        ){
            InfoBar(
                title = "Modo:",
                value = exerciseRoutine.exercise.mode.replaceFirstChar { it.uppercase() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 10.dp,
                        start = 20.dp,
                        end = 20.dp,
                    )
            )
            InfoBar(
                title = "Series:",
                value = "x${exerciseRoutine.sets}",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 10.dp,
                        start = 20.dp,
                        end = 20.dp,
                    )
            )
            InfoBar(
                title = "Repeticiones:",
                value = "${exerciseRoutine.minReps}-${exerciseRoutine.maxReps}",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 10.dp,
                        start = 20.dp,
                        end = 20.dp,
                    )
            )
            InfoBar(
                title = "Peso:",
                value = if (weight != 0.0) "${DecimalFormat("#.##").format(weight)} kg" else "Sin registrar",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 10.dp,
                        start = 20.dp,
                        end = 20.dp,
                        bottom = 10.dp
                    )
            ) {
                if (addWeight)
                    Image(
                        painter = painterResource(id = R.drawable.flecha_arriba),
                        contentDescription = null
                    )
                if (removeWeight)
                    Image(
                        painter = painterResource(id = R.drawable.flecha_abajo),
                        contentDescription = null
                    )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExerciseHistorical(historical: List<TrainingExercise>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)
            .background(
                MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(15.dp)
            )
    ) {
        items(historical) { entrenamiento ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(15.dp)
                    )
            ){
                entrenamiento.date?.let {
                    Text(
                        text = it.format(
                            DateTimeFormatter.ofPattern(
                                "d 'de' MMMM 'de' yyyy",
                                Locale("es", "ES")
                            )
                        ),
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = Color.White
                        ),
                        modifier = Modifier
                            .padding(horizontal = 15.dp, vertical = 15.dp)
                    )
                    var peso = DecimalFormat("#.##").format(entrenamiento.sets.first().weight)
                    val pesos = entrenamiento.sets.map {it.weight}
                    if (pesos.distinct().size != 1)
                        peso = pesos.joinToString(separator = ", ") { DecimalFormat("#.##").format(it) }
                    InfoBar(
                        title = "Peso:",
                        titleStyle = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.secondary
                        ),
                        value = "${peso} kg",
                        valueStyle = TextStyle(
                            color = Color.White,
                            fontSize = MaterialTheme.typography.bodySmall.fontSize
                        ),
                        modifier = Modifier.padding(start = 15.dp, end = 15.dp, bottom = 7.dp)
                    )
                    InfoBar(
                        title = "Repeticiones:",
                        titleStyle = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.secondary
                        ),
                        value = entrenamiento.sets.map { it.reps }.joinToString(separator = ", "),
                        valueStyle = TextStyle(
                            color = Color.White,
                            fontSize = MaterialTheme.typography.bodySmall.fontSize
                        ),
                        modifier = Modifier.padding(start = 15.dp, end = 15.dp, bottom = 12.dp)
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NextExercise(viewModel: TrainingViewModel) {
    val exerciseRoutine: ExerciseRoutine = viewModel.getActualExerciseRoutine()
    val exerciseHistorical: List<TrainingExercise> by viewModel.historical.observeAsState(initial = emptyList())
    viewModel.updateExerciseHistorical()

    val content = LocalContext.current
    BackHandler { viewModel.goBackInNextExerciseView(content) }

    Header(
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 30.dp)
        ){
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Siguiente:",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 15.dp)

            )
            ExerciseInfo(
                exerciseRoutine = exerciseRoutine,
                weight = exerciseHistorical.firstOrNull()?.getWeightAverage() ?: 0.0,
                addWeight = false,
                removeWeight = false)
            Text(
                text = "Historial:",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 25.dp, bottom = 15.dp)
            )
            ExerciseHistorical(exerciseHistorical)
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)) {
                Spacer(modifier = Modifier.weight(1F))
                Button(
                    onClick = { viewModel.startActualExerciseRoutine() },
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(15.dp)
                        )
                ) {
                    Text(
                        text = "EMPEZAR",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = Color.White
                        )
                    )
                }
                Spacer(modifier = Modifier.weight(1F))
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 25.dp)) {
                Button(
                    onClick = { viewModel.navigateToAddExerciseToTraining() },
                    modifier = Modifier
                        .weight(0.2f)
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.tertiary,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .height(35.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent, // Fondo transparente
                        contentColor = Color.White // Color del texto
                    )

                ) {
                    Text(
                        text = "Otro",
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = Color.White
                        )
                    )
                }
                Spacer(modifier = Modifier.weight(0.3f))
                Button(
                    onClick = { viewModel.skipExerciseInTraining() },
                    modifier = Modifier
                        .weight(0.2f)
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.tertiary,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .height(35.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent, // Fondo transparente
                        contentColor = Color.White // Color del texto
                    )

                ) {
                    Text(
                        text = "Saltar",
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = Color.White
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
fun PreviewNextEjercicio() {
    GimAppTheme {
        NextExercise(
            TrainingViewModel(DataBase(DaosDatabase_Impl()))
        )
    }
}