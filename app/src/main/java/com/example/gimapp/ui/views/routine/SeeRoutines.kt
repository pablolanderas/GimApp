package com.example.gimapp.ui.views.routine

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gimapp.data.database.DataBase
import com.example.gimapp.data.database.daos.DaosDatabase_Impl
import com.example.gimapp.ui.theme.GimAppTheme
import com.example.gimapp.ui.viewModels.RoutinesViewModel
import com.example.gimapp.ui.viewModels.TrainingViewModel
import com.example.gimapp.ui.views.components.Header
import com.example.gimapp.views.addTraining.DropListRooutine
import com.example.gimapp.views.addTraining.ListExercisesRutine
import com.example.gimapp.views.addTraining.SelectRoutine

@Composable
fun SeeRoutines(
    viewModel: RoutinesViewModel
) {

    val routines by viewModel.routines.observeAsState(initial = listOf())
    val selectedRoutine by viewModel.selectedRoutine.observeAsState()
    val seeAddRoutine by viewModel.seeAddRoutine.observeAsState(false)

    LaunchedEffect(Unit) {
        viewModel.launchRoutines()
    }

    if (seeAddRoutine) DialogAddRoutine(viewModel)

    Header() {

        Box(modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                val verticalMargin = 0.08f

                Spacer(modifier = Modifier.weight(verticalMargin))

                DropListRooutine(
                    options = routines,
                    selectedOption = selectedRoutine,
                    selected = false,
                    optionSelected = { viewModel.selectRoutine(it!!) },
                    lastOption = false
                )

                if (selectedRoutine != null)
                    ListExercisesRutine(
                        selectedRoutine,
                        Modifier
                            .weight(1f)
                            .padding(vertical = 50.dp)
                    )
                else Spacer(modifier = Modifier.weight(1f))

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
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
                    onClick = { viewModel.openAddRoutine() }
                ){
                    androidx.compose.material3.Text(
                        text = "Añadir rutina",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Spacer(modifier = Modifier.weight(verticalMargin))
            }
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showSystemUi = true)
@Composable
fun PreviewSelectRoutine() {
    GimAppTheme {
        SeeRoutines(RoutinesViewModel(DataBase(DaosDatabase_Impl())))
    }
}