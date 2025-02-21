package com.example.gimapp.ui

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gimapp.ui.viewModels.TrainingViewModel
import com.example.gimapp.domain.Exercise
import com.example.gimapp.ui.viewModels.ExercisesViewModel
import com.example.gimapp.ui.viewModels.HistoricalViewModel
import com.example.gimapp.ui.viewModels.managers.NavigateManager
import com.example.gimapp.ui.views.GimScreens
import com.example.gimapp.ui.views.exercises.ExerciseInfo
import com.example.gimapp.ui.views.exercises.SeeExercises
import com.example.gimapp.views.addTraining.AddExerciseToTraining
import com.example.gimapp.ui.views.addTraining.AddSet
import com.example.gimapp.ui.views.historical.SeeTraining
import com.example.gimapp.views.addTraining.DialogChangedRoutine
import com.example.gimapp.views.addTraining.DialogNameTraining
import com.example.gimapp.views.addTraining.EndRoutine
import com.example.gimapp.views.addTraining.NextExercise
import com.example.gimapp.views.menu.MainMenu
import com.example.gimapp.views.addTraining.SelectRoutine
import com.example.gimapp.views.historical.ShowHistorical
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GimAppController(
    private val trainingViewModel: TrainingViewModel,
    private val historicalViewModel: HistoricalViewModel,
    private val exercisesViewModel: ExercisesViewModel,
) {

    private lateinit var navController: NavHostController

    @SuppressLint("DefaultLocale", "CoroutineCreationDuringComposition")
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun StartApp() {

        navController = rememberNavController()
        NavigateManager.setNavigate(navController)

        NavHost(
            navController = navController,
            startDestination = GimScreens.Start.name
        ) {
            composable(route = GimScreens.Start.name) {
                val message: (@Composable (onClick: () -> Unit) -> Unit)? = trainingViewModel.getMenuMessage()
                var showMessage: Boolean by remember { mutableStateOf(true) }
                MainMenu(trainingViewModel)
                if (showMessage && message != null) {
                    message {
                        showMessage = false
                        trainingViewModel.resetMenuMessage()
                    }
                }
            }
            composable(route = GimScreens.SelectRoutine.name) {
                SelectRoutine(viewModel = trainingViewModel)
            }
            composable(route = GimScreens.NextExercise.name) {
                NextExercise(viewModel = trainingViewModel)
            }
            composable(route = GimScreens.OnSet.name) {
                AddSet(trainingViewModel)
            }
            composable(route = GimScreens.AddExerciseToTraining.name) {
                AddExerciseToTraining(viewModel = trainingViewModel)
            }
            composable(route = GimScreens.EndRoutine.name) {
                val showDialogUpdateRutine: Boolean by trainingViewModel.showChangesPopUp.observeAsState(initial = false)
                val showDialogEndNoRutine: Boolean by trainingViewModel.showNoRutinePopUp.observeAsState(initial = false)
                EndRoutine(viewModel = trainingViewModel)
                if (showDialogUpdateRutine) DialogChangedRoutine(viewModel = trainingViewModel)
                if (showDialogEndNoRutine) DialogNameTraining(viewModel = trainingViewModel)
            }
            composable(route = GimScreens.Historical.name) {
                ShowHistorical(historicalViewModel)
            }
            composable(route = GimScreens.SeeTraining.name) {
                SeeTraining(historicalViewModel)
            }
            composable(route = GimScreens.SeeExercises.name) {
                SeeExercises(exercisesViewModel)
            }
            composable(route = GimScreens.ExerciseInfo.name) {
                ExerciseInfo(exercisesViewModel)
            }
        }
    }

}