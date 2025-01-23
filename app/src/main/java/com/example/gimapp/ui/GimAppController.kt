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
import com.example.gimapp.ui.viewModels.AppViewModel
import com.example.gimapp.ui.viewModels.TrainingViewModel
import com.example.gimapp.domain.Exercise
import com.example.gimapp.ui.viewModels.HistoricalViewModel
import com.example.gimapp.ui.views.GimScreens
import com.example.gimapp.views.addTraining.AddExerciseToTraining
import com.example.gimapp.views.addTraining.AddSet
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
    private val badViewModel: AppViewModel = AppViewModel(),
) {

    private lateinit var navController: NavHostController

    @SuppressLint("DefaultLocale", "CoroutineCreationDuringComposition")
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun StartApp() {
        navController = rememberNavController()
        trainingViewModel.setNavigate { navController.navigate(it) }
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
                ShowHistorical(
                    historicalViewModel
                )
                /*
                trainings = badViewModel.getAllTrainings(),
                onClickTraining = {
                    badViewModel.setTrainingWatching(it)
                    navController.navigate(GimScreens.SeeTraining.name)
                }
                 */
            }
            composable(route = GimScreens.SeeTraining.name) {
                var exercises = emptyList<Exercise>()
                CoroutineScope(Dispatchers.IO).launch {
                    exercises = emptyList() // TODO
                }
                Text(
                    "$exercises",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .fillMaxHeight()
                        .padding(top = 300.dp)
                )
            }
            composable(route = GimScreens.AddExercise.name) {
                Text(text = "NUEVO ENTRENAMIENTO")
            }
        }
    }

}