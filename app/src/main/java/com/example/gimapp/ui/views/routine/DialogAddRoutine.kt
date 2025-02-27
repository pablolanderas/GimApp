package com.example.gimapp.ui.views.routine

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.gimapp.R
import com.example.gimapp.data.database.DataBase
import com.example.gimapp.data.database.daos.DaosDatabase_Impl
import com.example.gimapp.domain.Exercise
import com.example.gimapp.domain.ExerciseRoutine
import com.example.gimapp.domain.MuscularGroup
import com.example.gimapp.ui.theme.GimAppTheme
import com.example.gimapp.ui.viewModels.RoutinesViewModel

@Composable
fun ExerciseItem(
    exercise: ExerciseRoutine,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(5.dp)
            )
            .padding(5.dp)
    ){
        Text(
            text = exercise.exercise.name.replaceFirstChar { it.uppercase() }
                + " x${exercise.sets} (${exercise.minReps}-${exercise.maxReps})",
            style = MaterialTheme.typography.bodyMedium.copy(
                textAlign = TextAlign.Left
            ),
            modifier = Modifier.weight(1f)
        )
        Image(
            painter = painterResource(id = R.drawable.trash),
            contentDescription = "Delete",
            modifier = Modifier
                .height(24.dp)
                .clickable { onDelete() }
        )
    }
}

@Composable
fun ExerciseShower(
    exercises: List<ExerciseRoutine>,
    onAddExercise: () -> Unit,
    onDeleteExercise: (ExerciseRoutine) -> Unit,
    modifier: Modifier = Modifier
) {
    val itemsModifier = Modifier
        .fillMaxWidth()
        .padding(
            vertical = 5.dp,
            horizontal = 10.dp
        )
    LazyColumn(
        modifier
            .background(
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(vertical = 5.dp)
    ){
        items(exercises) {
            ExerciseItem(
                it,
                onDelete = { onDeleteExercise(it) },
                itemsModifier
            )
        }
        item {
            Row(
                itemsModifier
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(5.dp)
                    )
                    .clickable { onAddExercise() }
            ){
                Text(
                    text = "+",
                    style = MaterialTheme.typography.titleMedium.copy(
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun DialogAddRoutine(viewModel: RoutinesViewModel) {

    var routineName by remember { mutableStateOf("") }
    var exercises by remember { mutableStateOf(emptyList<ExerciseRoutine>()) }
    var showAddExercise by remember { mutableStateOf(false) }
    val actualMuscularGroup by viewModel.actualMuscularGroup.observeAsState(null)
    val exerciseOptions by viewModel.exerciseOptions.observeAsState(emptyList())
    var showSetModeAndData by remember { mutableStateOf(false) }
    val selectedExercise by viewModel.exerciseSelected.observeAsState()
    val modesOptions by viewModel.modesOptions.observeAsState(emptyList())

    if (showAddExercise) {
        DialogAddExerciseToRoutine(
            actualMuscularGroup,
            exerciseOptions,
            updateOptions = { viewModel.updateMuscularExercises(it) },
            onSelectedExercise = {
                viewModel.selectExercise(it)
                showSetModeAndData = true
                showAddExercise = false
            },
            onAddExercise = {},
            onCloseDialog = { showAddExercise = false }
        )
    }

    if (showSetModeAndData) {
        DialogSetModeAndData(
            selectedExercise!!,
            modesOptions,
            onExit = {
                showSetModeAndData = false
                showAddExercise = true
            },
            onAddModeToExercise = {},
            onConfirm = {
                showSetModeAndData = false
                exercises = exercises + it
            }
        )
    }

    Dialog(onDismissRequest = { viewModel.closeAddRoutine() } ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(.7f)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Nombre de la rutina",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color.White,
                    textAlign = TextAlign.Left
                ),
                modifier = Modifier.fillMaxWidth()
            )
            val focusManager = LocalFocusManager.current
            TextField(
                value = routineName,
                onValueChange = { routineName = it },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.secondary,
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondary,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.clearFocus() }
                ),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White
                ),
                modifier = Modifier
                    .padding(vertical = 10.dp)
            )
            ExerciseShower(
                exercises,
                onAddExercise = { showAddExercise = true },
                onDeleteExercise = { value -> exercises = exercises.filter { value != it } },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 10.dp)
                    .background(
                        color = MaterialTheme.colorScheme.secondary,
                        shape = RoundedCornerShape(10.dp)
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = Color.White,
                    disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                    disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                ),
                enabled = routineName.isNotEmpty() && exercises.isNotEmpty(),
                onClick = {  }
            ){
                Text(
                    text = "Crear rutina",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}


@Preview(showSystemUi = false)
@Composable
fun PreviewDialogAddRoutine() {
    GimAppTheme {
        DialogAddRoutine(RoutinesViewModel(DataBase(DaosDatabase_Impl())))
    }
}
