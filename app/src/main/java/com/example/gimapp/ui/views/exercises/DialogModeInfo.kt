package com.example.gimapp.ui.views.exercises

import android.icu.text.DecimalFormat
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.gimapp.data.database.DataBase
import com.example.gimapp.data.database.daos.DaosDatabase_Impl
import com.example.gimapp.domain.Exercise
import com.example.gimapp.domain.ExerciseSet
import com.example.gimapp.domain.MuscularGroup
import com.example.gimapp.domain.TrainingExercise
import com.example.gimapp.ui.theme.GimAppTheme
import com.example.gimapp.ui.viewModels.ExercisesViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


@Composable
fun RowHistorical(
    text1: String,
    text2: String,
    modifier: Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ){
        Text(text = text1, style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.weight(1f))
        Text(text = text2, style = MaterialTheme.typography.bodySmall.copy(color = Color.White))
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HistoricalElement(
    training: TrainingExercise,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(10.dp)
            )
    ){
        Column {
            Text(
                text = training.date!!.format(
                    DateTimeFormatter.ofPattern(
                        "d 'de' MMMM 'de' yyyy",
                        Locale("es", "ES")
                    )
                ),
                style = MaterialTheme.typography.titleSmall.copy(
                    color = Color.White
                ),
                modifier = Modifier
                    .padding(10.dp)
            )
            var peso = DecimalFormat("#.##").format(training.sets.first().weight)
            val pesos = training.sets.map {it.weight}
            if (pesos.distinct().size != 1)
                peso = pesos.joinToString(separator = ", ") { DecimalFormat("#.##").format(it) }
            RowHistorical(
                text1 = "Peso:",
                text2 = "${peso} kg",
                modifier = Modifier
                    .padding(bottom = 10.dp)
            )
            RowHistorical(
                text1 = "Repeticiones:",
                text2 = training.sets.map { it.reps }.joinToString(separator = ", "),
                modifier = Modifier
                    .padding(bottom = 10.dp)
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
var LISTA_ENTRENOS = listOf(
    TrainingExercise(
        Exercise("", "", MuscularGroup.Leg, null),
        LocalDate.of(2024, 11, 15),
        mutableListOf(
            ExerciseSet(30.0, 8, 0))
    ),
    TrainingExercise(
        Exercise("", "", MuscularGroup.Leg, null),
        LocalDate.of(2024, 11, 15),
        mutableListOf(
            ExerciseSet(30.0, 8, 0)
        )
    )
)


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DialogModeInfo(
    viewModel: ExercisesViewModel
) {

    val exercise by viewModel.selectedExercise.observeAsState(initial=NULL_EXERCISE)
    val exerciseHistorical by viewModel.exerciseHistorical.observeAsState(initial=LISTA_ENTRENOS)

    Dialog(onDismissRequest = { viewModel.closeDialogInfo() }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = exercise.mode.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(vertical = 10.dp)
            )
            Box(
                modifier = Modifier
                    .padding(
                        vertical = 15.dp,
                        horizontal = 5.dp
                    )
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(
                        color = MaterialTheme.colorScheme.secondary,
                        shape = RoundedCornerShape(20.dp)
                    )
            ) {
                LazyColumn (
                    modifier = Modifier
                        .padding(
                            horizontal = 7.dp,
                            vertical = 10.dp
                        )
                ) {
                    items(exerciseHistorical) {
                        HistoricalElement(
                            it,
                            Modifier.padding(vertical = 5.dp)
                        )
                    }
                }
            }

        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showSystemUi = false)
@Composable
fun PreviewDialogModeInfo() {
    GimAppTheme {
        DialogModeInfo(ExercisesViewModel(DataBase(DaosDatabase_Impl())))
    }
}