import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gimapp.AppViewModel
import com.example.gimapp.data.DatabBasev1
import com.example.gimapp.domain.Exercise
import com.example.gimapp.domain.ExerciseRutine
import com.example.gimapp.domain.ExerciseSet
import com.example.gimapp.domain.Rutine
import com.example.gimapp.domain.Training
import com.example.gimapp.domain.TrainingExercise
import com.example.gimapp.views.addTraining.AddExerciseToTraining
import com.example.gimapp.views.addTraining.AddSet
import com.example.gimapp.views.addTraining.AddSetState
import com.example.gimapp.views.addTraining.DialogChangedRutine
import com.example.gimapp.views.addTraining.DialogNameTraining
import com.example.gimapp.views.addTraining.DialogSetExerciseRutineToTraining
import com.example.gimapp.views.addTraining.EndRoutine
import com.example.gimapp.views.addTraining.NextExercise
import com.example.gimapp.views.menu.MainMenu
import com.example.gimapp.views.menu.MenuMessage
import com.example.gimapp.views.addTraining.SelectRutine
import kotlinx.coroutines.delay

enum class GimScreens() {
    Start,
    NextExercise,
    SelectRoutine,
    OnSet,
    AddExerciseToTraining,
    EndRoutine
}

class GimAppController(
    private val viewModel: AppViewModel = AppViewModel(DatabBasev1()),
) {

    private lateinit var navController: NavHostController

    @SuppressLint("DefaultLocale")
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun StartApp() {
        navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = GimScreens.Start.name
        ) {
            composable(route = GimScreens.Start.name) {
                val message: (@Composable (onClick: () -> Unit) -> Unit)? = viewModel.getMenuMesage()
                var showMessage: Boolean by remember { mutableStateOf(true) }
                Log.d("DEBUGGING", "El show: $showMessage")
                Log.d("DEBUGGING", "El msg: $message")
                MainMenu(
                    onNewTrainClicked = { navController.navigate(GimScreens.SelectRoutine.name) }
                )
                if (showMessage) {
                    if (message != null) {
                        message {
                            showMessage = false
                            viewModel.resetMenuMessage()
                        }
                    }
                }
            }
            composable(route = GimScreens.SelectRoutine.name) {
                SelectRutine(
                    onRutineSelected = {
                        viewModel.setActualRutine(it)
                        if (it != null)
                            navController.navigate(GimScreens.NextExercise.name)
                        else
                            navController.navigate(GimScreens.AddExerciseToTraining.name)
                    },
                    rutines = viewModel.getAllRutines()
                                .map { it as Rutine? } // Convierte cada elemento a Rutine?
                                .toMutableList() // Convierte la lista a mutable para añadirle el null
                                .apply {
                                    add(null) // Añade null al final
                                }
                )
            }
            composable(route = GimScreens.NextExercise.name) {
                val exerciseRutine: ExerciseRutine = viewModel.getNextExercise()
                NextExercise(
                    exerciseRutine = exerciseRutine,
                    historical = viewModel.getExerciseHistorical(exerciseRutine.exercise),
                    onSkipExercise = { nextExerciseInTraining(null) },
                    onStartExercise = { navController.navigate(GimScreens.OnSet.name) },
                    onOtherExercise = { navController.navigate(GimScreens.AddExerciseToTraining.name)}
                )
            }
            composable(route = GimScreens.OnSet.name) {
                val exerciseRutine: ExerciseRutine = viewModel.getNextExercise()
                var trainingExercise: TrainingExercise by remember {
                    mutableStateOf(
                        TrainingExercise(
                            exercise = exerciseRutine.exercise,
                            date = null,
                            sets = mutableStateListOf()
                        )
                    )
                }
                var remainingSets: Int by remember { mutableIntStateOf(viewModel.getRemainingSets()) }
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
                    lastExerciseSet = if (trainingExercise.sets.isNotEmpty()) trainingExercise.sets.last() else viewModel.getLastExerciseSet(exerciseRutine.exercise),
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
                        remainingSets = viewModel.getRemainingSets()
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
                        remainingSets = viewModel.getRemainingSets()
                    },
                    onPrevious = {
                        trainingExercise.sets.removeLast()
                        viewModel.addRemainingSet()
                        state = updateAddSetState(trainingExercise)
                        remainingSets = viewModel.getRemainingSets()
                    },
                    onAddSet = {
                        viewModel.addRemainingSet()
                        state = updateAddSetState(trainingExercise)
                        remainingSets = viewModel.getRemainingSets()
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
                            viewModel.setNoRutineExerciseRutine(exerciseRutine)
                            navController.navigate(GimScreens.NextExercise.name)
                        }
                    },
                    getExercises = { viewModel.getMuscleExercises(it) },
                    onAddExercise = { /*TODO*/ }
                )
            }
            composable(route = GimScreens.EndRoutine.name) {
                Log.d("DEBUGGING", "El end routine")
                var showDialogUpdateRutine by remember { mutableStateOf(false) }
                var showDialogEndNoRutine by remember { mutableStateOf(false) }
                EndRoutine(
                    training = viewModel.getActualTraining(),
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
        }
    }

    private fun nextExerciseInTraining(trainingExercise: TrainingExercise?) {
        val last: Boolean = viewModel.isLastRutineExercie()
        viewModel.setNextRutineExercise(trainingExercise)
        if (last) {
            if (viewModel.getActualTraining().exercises.isEmpty()) {
                viewModel.setMenuMessage { onClick ->
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
            viewModel.updateRutineToNextExercise()
            navController.navigate(GimScreens.NextExercise.name)
        }
    }

    private fun endAndCheckRemainingSets(
            trainingExercise: TrainingExercise,
            context: Context,
            message: (Int) -> String
    ) {
        val remaining: Int = viewModel.setEndedSet()
        if (remaining == 0) {
            nextExerciseInTraining(trainingExercise)
        } else {
            showToast(message(remaining), context)
        }
    }

    private fun updateAddSetState(trainingExercise: TrainingExercise): AddSetState {
        var remaining: Int = viewModel.getRemainingSets()
        if (remaining == 1 && trainingExercise.sets.size == 0) return AddSetState.Unique
        if (trainingExercise.sets.size == 0) return AddSetState.First
        if (remaining == 1) return AddSetState.Last
        return AddSetState.Normal
    }

    private fun processEndTraining( showChanges: () -> Unit, showNoRutine: () -> Unit ) {
        val rutine: Rutine? = viewModel.getActualRutine()
        val training: Training = viewModel.getActualTraining()
        if (rutine == null) {
            showNoRutine()
        } else {
            // Case in a rutine, compare the rutine and the training
            var equal: Boolean = true
            for (exerciseRutine in rutine.exercises) {
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
                viewModel.saveTraining(training)
                viewModel.setMenuMessage { onClick ->
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