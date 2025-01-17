import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gimapp.AppViewModel
import com.example.gimapp.TrainViewModel
import com.example.gimapp.data.DatabBasev1
import com.example.gimapp.data.database.DatabaseProvider
import com.example.gimapp.domain.Exercise
import com.example.gimapp.domain.ExerciseRutine
import com.example.gimapp.domain.ExerciseSet
import com.example.gimapp.domain.Routine
import com.example.gimapp.domain.Training
import com.example.gimapp.domain.TrainingExercise
import com.example.gimapp.views.addTraining.AddExerciseToTraining
import com.example.gimapp.views.addTraining.AddSet
import com.example.gimapp.views.addTraining.AddSetState
import com.example.gimapp.views.addTraining.DialogChangedRutine
import com.example.gimapp.views.addTraining.DialogNameTraining
import com.example.gimapp.views.addTraining.EndRoutine
import com.example.gimapp.views.addTraining.NextExercise
import com.example.gimapp.views.menu.MainMenu
import com.example.gimapp.views.menu.MenuMessage
import com.example.gimapp.views.addTraining.SelectRoutine
import com.example.gimapp.views.historical.ShowHistorical
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


enum class GimScreens() {
    Start,
    NextExercise,
    SelectRoutine,
    OnSet,
    AddExerciseToTraining,
    EndRoutine,
    Historical,
    SeeTraining
}


class GimAppController(
    private val goodViewModel: TrainViewModel,
    private val badViewModel: AppViewModel = AppViewModel(DatabBasev1()),
) {

    private lateinit var navController: NavHostController

    @SuppressLint("DefaultLocale", "CoroutineCreationDuringComposition")
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun StartApp() {
        navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = GimScreens.Start.name
        ) {
            composable(route = GimScreens.Start.name) {
                val message: (@Composable (onClick: () -> Unit) -> Unit)? = badViewModel.getMenuMesage()
                var showMessage: Boolean by remember { mutableStateOf(true) }
                Log.d("DEBUGGING", "El show: $showMessage")
                Log.d("DEBUGGING", "El msg: $message")
                MainMenu(
                    onNewTrainClicked = { navController.navigate(GimScreens.SelectRoutine.name) },
                    onHistoricalClicked = { navController.navigate(GimScreens.Historical.name) }
                )
                if (showMessage) {
                    if (message != null) {
                        message {
                            showMessage = false
                            badViewModel.resetMenuMessage()
                        }
                    }
                }
            }
            composable(route = GimScreens.SelectRoutine.name) {
                SelectRoutine(
                    onRoutineSelected = {
                        if (goodViewModel.routine.value != null)
                            navController.navigate(GimScreens.NextExercise.name)
                        else
                            navController.navigate(GimScreens.AddExerciseToTraining.name)
                    },
                    viewModel = goodViewModel
                )
            }
            composable(route = GimScreens.NextExercise.name) {
                NextExercise(
                    viewModel = goodViewModel,
                    onOtherExercise = { navController.navigate(GimScreens.NextExercise.name) },
                    onStartExercise = { navController.navigate(GimScreens.OnSet.name) },
                    onAddExercise = { navController.navigate(GimScreens.AddExerciseToTraining.name)},
                    onEndRoutine = { navController.navigate(GimScreens.EndRoutine.name) }
                )
                /*
                val exerciseRutine: ExerciseRutine = badViewModel.getNextExercise()
                exerciseRutine = exerciseRutine,
                historical = badViewModel.getExerciseHistorical(exerciseRutine.exercise),
                 */
            }
            composable(route = GimScreens.OnSet.name) {
                val exerciseRutine: ExerciseRutine = badViewModel.getNextExercise()
                var trainingExercise: TrainingExercise by remember {
                    mutableStateOf(
                        TrainingExercise(
                            exercise = exerciseRutine.exercise,
                            date = null,
                            sets = mutableStateListOf()
                        )
                    )
                }
                var remainingSets: Int by remember { mutableIntStateOf(badViewModel.getRemainingSets()) }
                var state: AddSetState by remember { mutableStateOf(updateAddSetState(trainingExercise)) }
                var timerString: String by remember { mutableStateOf("00:00") }
                var time: Int by remember { mutableIntStateOf(0) }
                // Timer
                LaunchedEffect(Unit) {
                    var hours = 0
                    while (true) {
                        time++
                        delay(1000L)
                        hours = time / 60
                        if (time < 6000)
                            timerString = "${String.format("%02d", hours)}:${String.format("%02d", time-hours*60)}"
                    }
                }
                // Component
                AddSet(
                    exerciseRutine = exerciseRutine,
                    trainingExercise = trainingExercise,
                    lastExerciseSet = if (trainingExercise.sets.isNotEmpty()) trainingExercise.sets.last() else badViewModel.getLastExerciseSet(exerciseRutine.exercise),
                    timerValue = timerString,
                    state = state,
                    remainingSets = remainingSets,
                    onNext = { weigth: Double, reps: Int, context: Context ->
                        trainingExercise.sets.add(ExerciseSet(weight = weigth, reps = reps, effort = -1))
                        endAndCheckRemainingSets(
                            trainingExercise = trainingExercise,
                            context = context,
                            message = { remaining -> "Se ha añadido correctamente\nFaltan ${remaining} series" }
                        )
                        state = updateAddSetState(trainingExercise)
                        remainingSets = badViewModel.getRemainingSets()
                        time = 0
                    },
                    onFailNext = {context -> showToast("Rellena los datos correctamente", context)},
                    onSkip = {
                        endAndCheckRemainingSets(
                            trainingExercise = trainingExercise,
                            context = it,
                            message = {remaining -> "\"Se ha saltado la serio\nFaltan ${remaining} series\""}
                        )
                        state = updateAddSetState(trainingExercise)
                        remainingSets = badViewModel.getRemainingSets()
                    },
                    onPrevious = {
                        trainingExercise.sets.removeLast()
                        badViewModel.addRemainingSet()
                        state = updateAddSetState(trainingExercise)
                        remainingSets = badViewModel.getRemainingSets()
                    },
                    onAddSet = {
                        badViewModel.addRemainingSet()
                        state = updateAddSetState(trainingExercise)
                        remainingSets = badViewModel.getRemainingSets()
                    },
                    onInfoSelected = {}
                )
            }
            composable(route = GimScreens.AddExerciseToTraining.name) {
                AddExerciseToTraining(
                    onContinue = { exerciseRutine, context ->
                        if (!exerciseRutine.isReal()) {
                            showToast("Los datos no son válidos", context)
                        } else {
                            badViewModel.setNoRutineExerciseRutine(exerciseRutine)
                            navController.navigate(GimScreens.NextExercise.name)
                        }
                    },
                    getExercises = { badViewModel.getMuscleExercises(it) },
                    onAddExercise = { /*TODO*/ }
                )
            }
            composable(route = GimScreens.EndRoutine.name) {
                Log.d("DEBUGGING", "El end routine")
                var showDialogUpdateRutine by remember { mutableStateOf(false) }
                var showDialogEndNoRutine by remember { mutableStateOf(false) }
                EndRoutine(
                    training = badViewModel.getActualTraining(),
                    onExtraExercise = { navController.navigate(GimScreens.AddExerciseToTraining.name) },
                    onEndTraining = {
                        processEndTraining(
                            showChanges =  { showDialogUpdateRutine = true },
                            showNoRutine = { showDialogEndNoRutine = true }
                        )
                    }
                )
                if (showDialogUpdateRutine) {
                    DialogChangedRutine(
                        onUpdate = { /*TODO*/ },
                        onCreate = { /*TODO*/ },
                        onNoRegister = { /*TODO*/ },
                        onExit = { showDialogUpdateRutine = false }
                    )
                }
                if (showDialogEndNoRutine) {
                    DialogNameTraining(
                        onSaveRutine = { Log.d("MainActivityDebuging", "Pulsado rutina con: $it") },
                        onSaveTraining = {
                            Log.d(
                                "MainActivityDebuging",
                                "Pulsado entrenamiento con: $it"
                            )
                        },
                        onExit = { showDialogEndNoRutine = false }

                    )
                }
            }
            composable(route = GimScreens.Historical.name) {
                ShowHistorical(
                    trainings = badViewModel.getAllTrainings(),
                    onClickTraining = {
                        badViewModel.setTrainingWatching(it)
                        navController.navigate(GimScreens.SeeTraining.name)
                    }
                )
            }
            composable(route = GimScreens.SeeTraining.name) {
                val context = LocalContext.current
                val database = DatabaseProvider.getDatabase(context)
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
        }
    }

    private fun nextExerciseInTraining(trainingExercise: TrainingExercise?) {
        val last: Boolean = badViewModel.isLastRutineExercie()
        badViewModel.setNextRutineExercise(trainingExercise)
        if (last) {
            if (badViewModel.getActualTraining().exercises.isEmpty()) {
                badViewModel.setMenuMessage { onClick ->
                    MenuMessage(
                        message = "El entrenamiento esta vacio\nNo se ha guardado",
                        exit = onClick
                    )
                }
                navController.navigate(GimScreens.Start.name)
            } else {
                Log.d("DEBUGGING", "En else superior")
                navController.navigate(GimScreens.EndRoutine.name)
            }
        } else {
            Log.d("DEBUGGING", "El else inferior")
            badViewModel.updateRutineToNextExercise()
            navController.navigate(GimScreens.NextExercise.name)
        }
    }

    private fun endAndCheckRemainingSets(
            trainingExercise: TrainingExercise,
            context: Context,
            message: (Int) -> String
    ) {
        val remaining: Int = badViewModel.setEndedSet()
        if (remaining == 0) {
            nextExerciseInTraining(trainingExercise)
        } else {
            showToast(message(remaining), context)
        }
    }

    private fun updateAddSetState(trainingExercise: TrainingExercise): AddSetState {
        var remaining: Int = badViewModel.getRemainingSets()
        if (remaining == 1 && trainingExercise.sets.size == 0) return AddSetState.Unique
        if (trainingExercise.sets.size == 0) return AddSetState.First
        if (remaining == 1) return AddSetState.Last
        return AddSetState.Normal
    }

    private fun processEndTraining( showChanges: () -> Unit, showNoRutine: () -> Unit ) {
        val routine: Routine? = badViewModel.getActualRutine()
        val training: Training = badViewModel.getActualTraining()
        if (routine == null) {
            showNoRutine()
        } else {
            // Case in a rutine, compare the rutine and the training
            var equal: Boolean = true
            for (exerciseRutine in routine.exercises) {
                // Buscamos el ejercicio en el entrenamiento que coincida con el de la rutina
                val trainingExercise =
                    training.exercises.find { it.exercise.name == exerciseRutine.exercise.name }
                // Si el ejercicio no está en el entrenamiento o el número de series no coincide, devolvemos falso
                if (trainingExercise == null || trainingExercise.sets.size != exerciseRutine.sets) {
                    equal = false
                    break
                }
            }
            if (equal) {
                badViewModel.saveTraining(training)
                badViewModel.setMenuMessage { onClick ->
                    MenuMessage(
                        message = "El entrenamiento se ha guardado correctamente",
                        exit = onClick
                    )
                }
                navController.navigate(GimScreens.Start.name)
            } else
                showChanges()
        }
    }

    private fun showToast(message: String, context: Context, length: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, message, length).show()
    }

}