package com.example.gimapp.ui.views.exercises

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gimapp.R
import com.example.gimapp.data.database.DataBase
import com.example.gimapp.data.database.daos.DaosDatabase_Impl
import com.example.gimapp.domain.Exercise
import com.example.gimapp.domain.MuscularGroup
import com.example.gimapp.ui.theme.GimAppTheme
import com.example.gimapp.ui.viewModels.ExercisesViewModel
import com.example.gimapp.ui.viewModels.TrainingViewModel
import com.example.gimapp.ui.views.components.Header
import com.example.gimapp.views.addTraining.SelectRoutine

val NULL_EXERCISE = Exercise("ejercicio de prueba", "el modo", MuscularGroup.Chest, null)
val LISTA_DE_PRUEBA = listOf("hola", "hola que tal", "hola que tal buenos dias, encantado de conocelo como andamos")

@Composable
fun ModeBox(
    viewModel: ExercisesViewModel,
    mode: String,
    modifier: Modifier = Modifier
) {
    GridBox(
        text = mode.replaceFirstChar { it.uppercase() },
        backgroundColor = MaterialTheme.colorScheme.secondary,
        onClick = { viewModel.showModeInfo(mode) },
        modifier = modifier
    )
}

@Composable
fun AddBox(
    viewModel: ExercisesViewModel,
    modifier: Modifier = Modifier
) {
    GridBox(
        text = "+",
        backgroundColor = Color.Gray,
        onClick = { viewModel.showAddMode() },
        modifier = modifier
    )
}

@Composable
fun GridBox(
    text: String,
    backgroundColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall.copy(
                color = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExerciseInfo(viewModel: ExercisesViewModel) {
    Header(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val exercise by viewModel.selectedExercise.observeAsState(initial=NULL_EXERCISE)
        val modes: List<String> by viewModel.selectedExerciseModes.observeAsState(initial=LISTA_DE_PRUEBA)
        val seeModeInfo: Boolean by viewModel.visibleDialogInfo.observeAsState(initial=false)
        val seeAddMode: Boolean by viewModel.visibleDialogAddMode.observeAsState(initial=false)

        if (seeModeInfo) { DialogModeInfo(viewModel) }

        if (seeAddMode) { DialogAddMode(viewModel) }

        Text(
            text = exercise.name.replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.titleLarge.copy(
                color = Color.White
            ),
            modifier = Modifier
                .padding(40.dp)
        )
        Image(
            painter = painterResource(id = exercise.imgURI ?: R.drawable.image),
            contentDescription = "Information botton",
            colorFilter = if (exercise.imgURI == null) {
                ColorFilter.tint(Color.White) // Aplica color blanco si es null
            } else null,
            modifier = Modifier
                .size(200.dp)
        )
        Box(
            modifier = Modifier
                .padding(40.dp)
                .fillMaxSize()
                .weight(1f)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(15.dp)
                    )
                    .padding(
                        horizontal = 15.dp,
                        vertical = 5.dp
                    )
                    .fillMaxSize()
            ) {
                val verticalPadding = 5.dp
                items(modes) { mode ->
                    ModeBox(
                        viewModel = viewModel,
                        mode = mode,
                        modifier = Modifier
                            .padding(vertical = verticalPadding)
                    )
                }
                item {
                    AddBox(
                        viewModel = viewModel,
                        modifier = Modifier
                            .padding(vertical = verticalPadding)
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showSystemUi = true)
@Composable
fun PreviewExerciseInfo() {
    GimAppTheme {
        ExerciseInfo(ExercisesViewModel(DataBase(DaosDatabase_Impl())))
    }
}