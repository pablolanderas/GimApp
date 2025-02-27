package com.example.gimapp.ui.views.addTraining

import android.icu.text.DecimalFormat
import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.gimapp.ui.views.components.Header
import com.example.gimapp.domain.Exercise
import com.example.gimapp.domain.ExerciseRoutine
import com.example.gimapp.domain.TrainingExercise
import com.example.gimapp.ui.theme.GimAppTheme
import com.example.gimapp.R
import androidx.compose.material3.TextFieldDefaults.colors
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.gimapp.ui.viewModels.TrainingViewModel
import com.example.gimapp.data.database.DataBase
import com.example.gimapp.data.database.daos.DaosDatabase_Impl
import com.example.gimapp.domain.ExerciseSet
import com.example.gimapp.domain.MuscularGroup
import com.example.gimapp.ui.views.exercises.HistoricalElement
import com.example.gimapp.ui.views.exercises.LISTA_ENTRENOS
import com.example.gimapp.ui.views.exercises.NULL_EXERCISE
import com.example.gimapp.ui.views.exercises.RowHistorical
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun TitleRow(
    exerciseRoutine: ExerciseRoutine,
    onPressInfo: () -> Unit,
    onUnpressInfo: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp, start = 20.dp, end = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.size(20.dp))
        Text(
            text = exerciseRoutine.exercise.name.replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            textAlign = TextAlign.Center,
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
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            Log.d(
                                "MainActivityDebuging",
                                "Antes de soltar"
                            ) // Acción antes de soltar
                            onPressInfo()
                            tryAwaitRelease() // Espera hasta que se suelte el toque
                            Log.d(
                                "MainActivityDebuging",
                                "Después de soltar"
                            ) // Acción después de soltar
                            onUnpressInfo()
                        }
                    )
                }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InfoDialog(
    exercise: Exercise,
    exerciseHistorical: List<TrainingExercise>
) {

    Dialog(onDismissRequest = { }) {
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

@Composable
fun WeightAndRepsSelecter(
    modifier: Modifier = Modifier,
    weightValue: String,
    repsValue: String,
    onValueChangeWeight: (String) -> Unit,
    onValueChangeReps: (String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
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
            onNext = { focusRequester.requestFocus() },
            onValueChange = onValueChangeWeight
        )
        ColumOfTextSelecter(
            text = "Repeticiones",
            modifier = Modifier.weight(1f),
            value = repsValue,
            focusRequester = focusRequester,
            onNext = { focusManager.clearFocus() },
            onValueChange = onValueChangeReps
        )
    }
}

@Composable
fun ColumOfTextSelecter(
    text : String,
    modifier: Modifier,
    value: String,
    focusRequester: FocusRequester? = null,
    onNext: () -> Unit,
    onValueChange: (String) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = { onValueChange(it) },
            colors = colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondary,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            textStyle = MaterialTheme.typography.bodyMedium
                .copy(color = Color.White),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number, // Configurar el teclado para que sea numérico
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { onNext() }
            ),
            modifier = Modifier
                .padding(vertical = 20.dp, horizontal = 30.dp)
                .fillMaxWidth() // Asegúrate de que el OutlinedTextField ocupe el espacio necesario
                .then(
                    if (focusRequester != null) Modifier.focusRequester(focusRequester)
                    else Modifier
                )
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
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
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
                    val textStyle = MaterialTheme.typography.bodySmall
                    Text(
                        text = "${index+1}.",
                        style = textStyle,
                        modifier = Modifier
                            .padding(5.dp)

                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "${if (set.weight % 1.0 == 0.0) set.weight.toInt() else set.weight}kg x ${set.reps}",
                        style = textStyle,
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
    modifier: Modifier,
    timerValue: String
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
                text = timerValue,
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
    state: AddSetState,
    onSkip: () -> Unit,
    onPrevious: () -> Unit,
    onAddSet: () -> Unit
) {
    Row(modifier = modifier
        .fillMaxWidth()
        .padding(vertical = 25.dp)) {
        if (state != AddSetState.First && state != AddSetState.Unique)
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
        else
            Spacer(modifier = Modifier.weight(0.2f))
        if (state == AddSetState.Last || state == AddSetState.Unique) {
            Spacer(modifier = Modifier.weight(0.05f))
            Button(
                onClick = onAddSet,
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
                    text = "Otra",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.weight(0.05f))
        }
        else
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

enum class AddSetState {
    First,
    Normal,
    Last,
    Unique
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddSet(
    viewModel: TrainingViewModel
) {
    val context = LocalContext.current
    val lastExerciseSet: ExerciseSet? = viewModel.getActualExcerciseRutineLastExerciseSet()
    var weightValue: String by remember {
        mutableStateOf(lastExerciseSet?.weight
                        ?.let {
                            if (it % 1.0 == 0.0) it.toInt().toString()
                            else it.toString()
                        } ?: "")
    }
    var repsValue: String by remember { mutableStateOf(lastExerciseSet?.reps?.toString() ?: "") }
    val timerValue: String by viewModel.timer.observeAsState(initial = "00:00")
    val remainingSets: Int by viewModel.remainingExerciseSets.observeAsState(initial = 0)
    val trainingExercise: TrainingExercise by viewModel.actualTrainingExercise.observeAsState(
        initial = TrainingExercise(Exercise("","",MuscularGroup.Abs), null, mutableListOf<ExerciseSet>())
    )
    val state: AddSetState by viewModel.stateOfAddSet.observeAsState(initial = AddSetState.Normal)
    val exerciseHistorical: List<TrainingExercise> by viewModel.historical.observeAsState(initial = emptyList())
    viewModel.startTimer()

    BackHandler { viewModel.goBackInAddSet(context) }

    Header(
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {
        var showDialog by remember { mutableStateOf(false) }
        if (showDialog)
            InfoDialog(
                exercise = viewModel.getActualExerciseRoutine().exercise,
                exerciseHistorical
            )
        TitleRow(
            viewModel.getActualExerciseRoutine(),
            onPressInfo = { showDialog = true },
            onUnpressInfo = { showDialog = false }
        )
        Spacer(modifier = Modifier.weight(1f))
        WeightAndRepsSelecter(
            modifier = Modifier.padding(horizontal = 20.dp),
            weightValue = weightValue,
            repsValue = repsValue,
            onValueChangeWeight = { newValue ->
                val regex = """^\d+(\.\d*)?$""".toRegex()
                if (newValue.isEmpty() || (newValue.matches(regex) && newValue.length <= 6)) {
                    weightValue = newValue
                } else if (newValue.length > 1 && newValue.count { it == '.' } == 1 && newValue.lastOrNull() in listOf(',', '.')) {
                    weightValue = newValue.dropLast(1) + "."
                }
            },
            onValueChangeReps = { newValue ->
                if ((newValue.isEmpty() || newValue.all { it.isDigit() }) && newValue.length <= 5) {
                    repsValue = newValue
                }
            }

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
                modifier = Modifier.weight(1f),
                timerValue = timerValue
            )
        }
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            text = if (remainingSets > 1) "Quedan $remainingSets series" else "Útima serie",
            style = MaterialTheme.typography.bodySmall,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(modifier = Modifier
            .fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { viewModel.tringToAddSet(weightValue, repsValue, context) },
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(15.dp)
                    )
            ) {
                Text(
                    text = if (state != AddSetState.Last) "SIGUIENTE" else "ACABAR",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        }
        NavigateButtons(
            modifier = Modifier.padding(horizontal = 20.dp),
            state = state,
            onSkip = { viewModel.skipAddSet(context) },
            onPrevious = { viewModel.previousSet() },
            onAddSet = { viewModel.addExtraSet() }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showSystemUi = true)
@Composable
fun PrevieAddSet() {
    GimAppTheme {
        AddSet(TrainingViewModel(DataBase(DaosDatabase_Impl())))
    }
}

