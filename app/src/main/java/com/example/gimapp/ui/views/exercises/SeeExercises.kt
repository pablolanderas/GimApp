package com.example.gimapp.ui.views.exercises

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.example.gimapp.data.database.DataBase
import com.example.gimapp.data.database.daos.DaosDatabase_Impl
import com.example.gimapp.domain.Exercise
import com.example.gimapp.domain.MuscularGroup
import com.example.gimapp.ui.theme.GimAppTheme
import com.example.gimapp.ui.viewModels.ExercisesViewModel
import com.example.gimapp.ui.views.components.ExercisesDisplay
import com.example.gimapp.ui.views.components.Header


@Composable
fun SeeExercises(viewModel: ExercisesViewModel) {
    Header(
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {

        val muscularGroup: MuscularGroup? by viewModel.muscularGroup.observeAsState(initial=null)
        val exercisesOptions: List<Exercise> by viewModel.exercisesOptions.observeAsState(initial=emptyList())

        Text(
            text = "Ejercicios",
            style = MaterialTheme.typography.titleLarge.copy(
                color = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp)
        )
        ExercisesDisplay(
            muscularGroup = muscularGroup,
            actualOptions = exercisesOptions,
            onChangedMuscularGroup = { viewModel.updateMuscularGroup(it) },
            onSelectedExercise = { },
            onAddExercise = { },
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp)
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewSeeExercises() {
    GimAppTheme {
        SeeExercises(ExercisesViewModel(DataBase(DaosDatabase_Impl())))
    }
}