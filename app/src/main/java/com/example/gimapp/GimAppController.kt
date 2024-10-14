import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
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
import com.example.gimapp.domain.ExerciseRutine
import com.example.gimapp.domain.ExerciseSet
import com.example.gimapp.domain.TrainingExercise
import com.example.gimapp.views.AddSet
import com.example.gimapp.views.AddSetState
import com.example.gimapp.views.NextExercise
import com.example.gimapp.views.MainMenu
import com.example.gimapp.views.SelectRutine
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
    private val viewModel: AppViewModel = AppViewModel(), // Asignación adecuada en el constructor
) {

    private lateinit var navController: NavHostController // Debe ser proporcionado externamente

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
                MainMenu (
                    onNewTrainClicked = { navController.navigate(GimScreens.SelectRoutine.name) }
                )
            }
            composable(route = GimScreens.SelectRoutine.name) {
                SelectRutine(
                    onRutineSelected = {
                        viewModel.setActualRutine(it)
                        navController.navigate(GimScreens.NextExercise.name)
                    },

                    rutines = viewModel.getAllRutines()
                )
            }
            composable(route = GimScreens.NextExercise.name) {
                val exerciseRutine: ExerciseRutine = viewModel.getRutineExercise()
                NextExercise(
                    exerciseRutine = exerciseRutine,
                    historical = viewModel.getExerciseHistorical(exerciseRutine.exercise),
                    onSkipExercise = { nextExerciseInTraining(null) },
                    onStartExercise = { navController.navigate(GimScreens.OnSet.name) },
                    onOtherExercise = { navController.navigate(GimScreens.AddExerciseToTraining.name)}
                )
            }
            composable(route = GimScreens.OnSet.name) {
                val exerciseRutine: ExerciseRutine = viewModel.getRutineExercise()
                var trainingExercise: TrainingExercise by remember {
                    mutableStateOf(
                        TrainingExercise(
                            exercise = exerciseRutine.exercise,
                            date = null,
                            sets = mutableStateListOf()
                        )
                    )
                }
                var state: AddSetState by remember { mutableStateOf(updateAddSetState(trainingExercise)) }
                var timerString: String by remember { mutableStateOf("00:00") }
                var time: Int by remember { mutableIntStateOf(0) }

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

                AddSet(
                    exerciseRutine = exerciseRutine,
                    trainingExercise = trainingExercise,
                    lastExerciseSet = if (trainingExercise.sets.isNotEmpty()) trainingExercise.sets.last() else viewModel.getLastExerciseSet(exerciseRutine.exercise),
                    timerValue = timerString,
                    state = state,
                    onNext = { weigth: Double, reps: Int, context: Context ->
                        trainingExercise.sets.add(ExerciseSet(weight = weigth, reps = reps, effort = -1))
                        endAndCheckRemainingSets(
                            trainingExercise = trainingExercise,
                            context = context,
                            message = { remaining -> "Se ha añadido correctamente\nFaltan ${remaining} series" }
                        )
                        state = updateAddSetState(trainingExercise)
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
                    },
                    onPrevious = {
                        trainingExercise.sets.removeLast()
                        viewModel.addRemainingSet()
                        state = updateAddSetState(trainingExercise)
                    },
                    onAddSet = {
                        viewModel.addRemainingSet()
                        state = updateAddSetState(trainingExercise)
                    },
                    onInfoSelected = {}
                )
            }
            composable(route = GimScreens.AddExerciseToTraining.name) {

            }
            composable(route = GimScreens.EndRoutine.name) {

            }
        }
    }

    private fun nextExerciseInTraining(trainingExercise: TrainingExercise?) {
        val last: Boolean = viewModel.isLastRutineExercie()
        viewModel.setNextRutineExercise(trainingExercise)
        if (last) {
            navController.navigate(GimScreens.EndRoutine.name)
        } else {
            viewModel.updateRutineToNextExercise()
            navController.navigate(GimScreens.NextExercise.name)
        }
    }

    private fun endAndCheckRemainingSets(
            trainingExercise: TrainingExercise,
            context: Context,
            message: (Int) -> String) {
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

    private fun showToast(message: String, context: Context, length: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, message, length).show()
    }

}