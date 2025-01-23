package com.example.gimapp.views.addTraining

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults.colors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.gimapp.ui.viewModels.TrainingViewModel
import com.example.gimapp.data.database.DataBase
import com.example.gimapp.data.database.daos.DaosDatabase_Impl
import com.example.gimapp.domain.Exercise
import com.example.gimapp.domain.ExerciseRoutine
import com.example.gimapp.ui.theme.GimAppTheme

@Composable
fun ThisOutlinedTextField(
    setValue: (Int?) -> Unit,
    maxNumberLength: Int,
    focusRequester: FocusRequester?,
    modifier: Modifier = Modifier,
    onNext: (() -> Unit)? = null,
    onDone: (() -> Unit)? = null,
    imeAction: ImeAction = ImeAction.Next
) {
    var numValue by remember { mutableStateOf("") }
    OutlinedTextField(
        value = numValue,
        onValueChange = { newValue ->
            if ((newValue.isEmpty() || newValue.all { it.isDigit() }) && newValue.length <= maxNumberLength) {
                numValue = newValue
                setValue(newValue.toIntOrNull())
            }
        },
        colors = colors(
            focusedContainerColor = MaterialTheme.colorScheme.secondary,
            unfocusedContainerColor = MaterialTheme.colorScheme.secondary,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            color = Color.White,
            textAlign = TextAlign.Center
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                onNext?.invoke()
            },
            onDone = {
                onDone?.invoke()
            },
        ),
        modifier = modifier
            .then(
                if (focusRequester != null) modifier.focusRequester(focusRequester)
                else modifier
            )
    )
}

@Composable
fun DataAdder(
    description: String,
    setValue: (Int?) -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .align(Alignment.CenterVertically)
        )
        Spacer(modifier = Modifier.weight(1f))
        ThisOutlinedTextField(
            setValue = setValue,
            maxNumberLength = 3,
            focusRequester = null,
            onNext = onNext,
            modifier = Modifier
                .width(80.dp)
        )
    }
}

@Composable
fun RepsSelector(
    setMinReps: (Int?) -> Unit,
    setMaxReps: (Int?) -> Unit,
    minRepsFocusRequester: FocusRequester,
    maxRepsFocusRequester: FocusRequester,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = "Repeticiones",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .align(Alignment.CenterVertically)
        )
        Spacer(modifier = Modifier.weight(1f))
        ThisOutlinedTextField(
            setValue = setMinReps,
            maxNumberLength = 2,
            focusRequester = minRepsFocusRequester,
            onNext = { maxRepsFocusRequester.requestFocus() },
            modifier = Modifier
                .width(60.dp)
        )
        Text(
            text = "-",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(horizontal = 10.dp)
        )
        val focusManager = LocalFocusManager.current
        ThisOutlinedTextField(
            setValue = setMaxReps,
            maxNumberLength = 2,
            focusRequester = maxRepsFocusRequester,
            onDone = {
                focusManager.clearFocus()
            },
            imeAction = ImeAction.Done,
            modifier = Modifier
                .width(60.dp)
        )
    }
}

@Composable
fun DialogSetExerciseRutineToTraining(
    exercise: Exercise,
    viewModel: TrainingViewModel,
    goNextExercise: () -> Unit,
    onExit: () -> Unit
) {
    val focusMinRep = remember { FocusRequester() }
    val focusMaxRep = remember { FocusRequester() }
    var numSets: Int? by remember { mutableStateOf(null) }
    var minReps: Int? by remember { mutableStateOf(null) }
    var maxReps: Int? by remember { mutableStateOf(null) }
    Dialog(onDismissRequest = { onExit() } ) {
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
                text = "Añadiendo ${exercise.name.replaceFirstChar { it.uppercase() } }",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            DataAdder(
                description = "Número de series:",
                setValue = { numSets = it},
                onNext = { focusMinRep.requestFocus() },
                modifier = Modifier
                    .padding(top = 20.dp)
            )
            RepsSelector(
                setMinReps = { minReps = it },
                setMaxReps = { maxReps = it },
                minRepsFocusRequester = focusMinRep,
                maxRepsFocusRequester = focusMaxRep,
                modifier = Modifier
                    .padding(top = 20.dp)
            )
            val context: Context = LocalContext.current
            Button(
                onClick = {
                    if (numSets != null && minReps != null && maxReps != null)
                        viewModel.setOtherActualExerciseRoutine(
                            e = ExerciseRoutine(exercise, numSets!!, minReps!!, maxReps!!),
                            goNextExercise = goNextExercise,
                            context = context
                        )
                },
                shape = RoundedCornerShape(20), // Hace el botón redondo
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                modifier = Modifier
                    .padding(top = 20.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Añadir",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White,
                    )
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewDialogSetExerciseRutineToTraining() {
    GimAppTheme {
        Spacer(modifier = Modifier.fillMaxSize())
        DialogSetExerciseRutineToTraining (
            Exercise("press banca", "si"),
            TrainingViewModel(DataBase(DaosDatabase_Impl())),
            {},
            {}
        )
    }
}