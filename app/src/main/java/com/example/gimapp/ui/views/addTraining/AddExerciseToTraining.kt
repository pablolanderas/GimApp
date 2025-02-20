package com.example.gimapp.views.addTraining

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gimapp.ui.viewModels.TrainingViewModel
import com.example.gimapp.ui.views.components.Header
import com.example.gimapp.data.database.DataBase
import com.example.gimapp.data.database.daos.DaosDatabase_Impl
import com.example.gimapp.domain.Exercise
import com.example.gimapp.domain.MuscularGroup
import com.example.gimapp.ui.theme.GimAppTheme
import com.example.gimapp.ui.viewModels.managers.NavigateManager
import com.example.gimapp.ui.views.GimScreens
import com.example.gimapp.ui.views.components.ExercisesDisplay
import com.example.gimapp.ui.views.exercises.DialogAddExercise
import com.example.gimapp.ui.views.exercises.DialogAddMode

@Composable
fun AddExerciseToTraining(
    viewModel: TrainingViewModel
) {
    Header(
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

        var exercise: Exercise? by remember { mutableStateOf(null) }
        var showDialog: Boolean by remember { mutableStateOf(false) }
        val muscularGroup: MuscularGroup? by viewModel.muscularGroup.observeAsState(initial=null)
        val exerciseList by viewModel.exercises.observeAsState(initial=emptyList())
        val showAddMode: Boolean by viewModel.showAddMode.observeAsState(initial=false)
        val showAddExercise: Boolean by viewModel.showAddExercise.observeAsState(initial=false)

        BackHandler { viewModel.goBackInAddExerciseToTraining() }

        if (showAddMode) { DialogAddMode(viewModel) }
        if (showAddExercise) { DialogAddExercise(viewModel) }
        if (showDialog) {
            DialogSetExerciseRutineToTraining (
                exercise = exercise ?: throw Error("The exercise is not selected and the dialog was called"),
                viewModel = viewModel,
                goNextExercise = {
                    viewModel.navigateToNextExerciseSinceAddExercise()
                    showDialog = false
                },
                onAddModeToExercise = { viewModel.openAddMode(exercise!!) },
                onExit = { showDialog = false }
            )
        }
        Text(
            text = "Selecciona el ejercicio",
            style = MaterialTheme.typography.titleLarge.copy(
                color = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp)
        )
        ExercisesDisplay(
            muscularGroup = muscularGroup,
            actualOptions = exerciseList,
            onChangedMuscularGroup = { viewModel.updateMuscleExercises(it) },
            onSelectedExercise = {
                exercise = it
                showDialog = true
            },
            onAddExercise = { viewModel.openAddExercise() },
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp)
        )
        Spacer(modifier = Modifier.height(50.dp))
    }
}


@Preview(showSystemUi = true)
@Composable
fun PreviewAddExerciseToTraining() {
    GimAppTheme {
        AddExerciseToTraining(TrainingViewModel(DataBase(DaosDatabase_Impl())))
    }
}