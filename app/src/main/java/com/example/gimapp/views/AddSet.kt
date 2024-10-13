package com.example.gimapp.views

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gimapp.components.Header
import com.example.gimapp.domain.Exercise
import com.example.gimapp.domain.ExerciseRutine
import com.example.gimapp.domain.TrainingExercise
import com.example.gimapp.ui.theme.GimAppTheme
import com.example.gimapp.R
import androidx.compose.material3.TextFieldDefaults.colors
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import com.example.gimapp.domain.ExerciseSet
import kotlinx.coroutines.delay

@Composable
fun TitleRow(
    exerciseRutine: ExerciseRutine
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp, start = 20.dp, end = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.size(20.dp))
        Text(
            text = exerciseRutine.exercise.name.replaceFirstChar { it.uppercase() },
            color = MaterialTheme.colorScheme.primary,
            style = TextStyle(
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f)
        )
        Image(
            painter = painterResource(id = R.drawable.info_icon),
            contentDescription = "Information botton",
            modifier = Modifier
                .size(20.dp)
                .align(Alignment.CenterVertically)
        )
    }
}

@Composable
fun WeightAndRepsSelecter(
    modifier: Modifier = Modifier,
    lastExerciseSet: ExerciseSet? = null,
    weightValue: String,
    repsValue: String,
    onValueChangeWeight: (String) -> Unit,
    onValueChangeReps: (String) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(20.dp)
            )
    ) {
        ColumOfTextSelecter(
            text = "Peso",
            modifier = Modifier.weight(1f),
            value = weightValue,
            onValueChange = onValueChangeWeight
        )
        ColumOfTextSelecter(
            text = "Repeticiones",
            modifier = Modifier.weight(1f),
            value = repsValue,
            onValueChange = onValueChangeReps
        )
    }
}

@Composable
fun ColumOfTextSelecter(
    text : String,
    modifier: Modifier,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = text,
            style = TextStyle(
                color = Color.White,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = { newValue ->
                if ((newValue.isEmpty() || newValue.all { it.isDigit() }) && newValue.length <= 5) {
                    onValueChange(newValue)
                }
            },
            colors = colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondary,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            textStyle = TextStyle(
                color = Color.White,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                textAlign = TextAlign.Center
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number // Configurar el teclado para que sea numérico
            ),
            modifier = Modifier
                .padding(vertical = 20.dp, horizontal = 30.dp)
                .fillMaxWidth() // Asegúrate de que el OutlinedTextField ocupe el espacio necesario
        )
    }
}

@Composable
fun MiniBox(
    modifier: Modifier,
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = title,
            style = TextStyle(
                color = Color.White,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize
            ),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(vertical = 10.dp)
                .background(
                    color = MaterialTheme.colorScheme.secondary,
                    shape = RoundedCornerShape(20.dp)
                ),
            content = content
        )
    }
}

@Composable
fun ExerciseHistorical(
    trainingExercise: TrainingExercise,
    modifier: Modifier
) {
    MiniBox(
        modifier = modifier,
        title = "Anteriores"
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)
        ) {
            itemsIndexed(trainingExercise.sets) { index, set ->
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    Text(
                        text = "${index+1}.",
                        style = TextStyle(
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier
                            .padding(5.dp)

                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "${if (set.weight % 1.0 == 0.0) set.weight.toInt() else set.weight}kg x ${set.reps}",
                        style = TextStyle(
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier
                            .padding(5.dp)

                    )
                }
            }
        }
    }
}

@Composable
fun Timer(
    modifier: Modifier
) {
    MiniBox(
        modifier = modifier,
        title = "Tiempo"
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "00:30",
                color = Color.White,
                style = TextStyle(
                    fontSize = 40.sp,
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}

@Composable
fun NavigateButtons(
    modifier: Modifier,
    onSkip: () -> Unit,
    onPrevious: () -> Unit
) {
    Row(modifier = modifier
        .fillMaxWidth()
        .padding(vertical = 25.dp)) {
        Button(
            onClick = onPrevious,
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
                text = "Anterior",
                style = MaterialTheme.typography.titleSmall.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            )
        }
        Spacer(modifier = Modifier.weight(0.3f))
        Button(
            onClick = onSkip,
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
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
fun AddSet(
    exerciseRutine: ExerciseRutine,
    trainingExercise: TrainingExercise,
    onNext: (Double, Int, Context) -> Unit,
    onFailNext: (Context) -> Unit,
    onSkip: (Context) -> Unit,
    onPrevious: (Context) -> Unit,
    lastExerciseSet: ExerciseSet? = null
) {
    val context = LocalContext.current
    val weightValue: MutableState<String> = remember {
        mutableStateOf(
            lastExerciseSet?.weight?.let { if (it % 1.0 == 0.0) it.toInt().toString() else it.toString() } ?: ""
        )
    }
    val repsValue: MutableState<String> = remember { mutableStateOf(lastExerciseSet?.reps?.toString() ?: "") }
    Header(
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {
        Column {
            TitleRow(exerciseRutine)
            Spacer(modifier = Modifier.weight(1f))
            WeightAndRepsSelecter(
                modifier = Modifier.padding(horizontal = 20.dp),
                lastExerciseSet = lastExerciseSet,
                weightValue = weightValue.value,
                repsValue = repsValue.value,
                onValueChangeWeight = { weightValue.value = it },
                onValueChangeReps = { repsValue.value = it }
            )
            Spacer(modifier = Modifier.weight(0.3f))
            Row(
                modifier = Modifier
            ) {
                ExerciseHistorical(
                    trainingExercise = trainingExercise,
                    modifier = Modifier.weight(1f)
                )
                Timer(
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(modifier = Modifier
                .fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = {
                        val weight: Double? = weightValue.value.toDoubleOrNull()
                        val reps: Int? = repsValue.value.toIntOrNull()
                        if (weight != null && reps != null)
                            onNext(weight, reps, context)
                        else
                            onFailNext(context)
                    },
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(15.dp)
                        )
                ) {
                    Text(
                        text = "SIGUIENTE",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            }
            NavigateButtons(
                modifier = Modifier.padding(horizontal = 20.dp),
                onSkip = { onSkip(context) },
                onPrevious = { onPrevious(context) }
            )
        }
    }
}

@Preview(showSystemUi = true, )
@Composable
fun PrevieAddSet() {
    GimAppTheme {
        AddSet(
            exerciseRutine = ExerciseRutine(
                exercise = Exercise("press banca", "con banca"),
                sets = 4,
                minReps = 8,
                maxReps = 10
            ),
            trainingExercise = TrainingExercise(
                exercise = Exercise("press banca", "con banca"),
                date = null,
                sets = mutableListOf(ExerciseSet(40.0, 8, 0), ExerciseSet(40.0, 8, 0))
            ),
            onNext = { s1, s2, s3 -> {} },
            onFailNext = {},
            onSkip = {},
            onPrevious = {}
        )
    }
}

