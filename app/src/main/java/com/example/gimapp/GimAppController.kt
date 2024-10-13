import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gimapp.AppViewModel
import com.example.gimapp.domain.ExerciseRutine
import com.example.gimapp.domain.ExerciseSet
import com.example.gimapp.domain.TrainingExercise
import com.example.gimapp.views.AddSet
import com.example.gimapp.views.NextExercise
import com.example.gimapp.views.MainMenu
import com.example.gimapp.views.SelectRutine

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
                val trainingExercise = TrainingExercise(
                    exercise = exerciseRutine.exercise,
                    date = null,
                    sets = mutableListOf()
                )

                AddSet(
                    exerciseRutine = exerciseRutine,
                    trainingExercise = trainingExercise,
                    lastExerciseSet = if (trainingExercise.sets.isNotEmpty()) trainingExercise.sets.last() else null,
                    onNext = { weigth: Double, reps: Int, context: Context ->
                        trainingExercise.sets.add(ExerciseSet(weight = weigth, reps = reps, effort = -1))
                        endAndCheckRemainingSets(
                            trainingExercise = trainingExercise,
                            context = context,
                            message = { remaining -> "Se ha añadido correctamente\nFaltan ${remaining} series" }
                        )
                    },
                    onFailNext = {context -> Toast.makeText(context, "Rellena los datos correctamente", Toast.LENGTH_SHORT).show()},
                    onSkip = {
                        endAndCheckRemainingSets(
                            trainingExercise = trainingExercise,
                            context = it,
                            message = {remaining -> "\"Se ha saltado la serio\nFaltan ${remaining} series\""}
                        )
                    },
                    onPrevious = {}
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
            Toast.makeText(context, message(remaining), Toast.LENGTH_SHORT).show()
        }
    }

}