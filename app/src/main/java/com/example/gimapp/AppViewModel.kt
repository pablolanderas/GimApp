package com.example.gimapp

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import com.example.gimapp.data.AppUiState
import com.example.gimapp.domain.Exercise
import com.example.gimapp.domain.ExerciseRutine
import com.example.gimapp.domain.ExerciseSet
import com.example.gimapp.data.IDataBase
import com.example.gimapp.domain.MuscularGroup
import com.example.gimapp.domain.Rutine
import com.example.gimapp.domain.Training
import com.example.gimapp.domain.TrainingExercise
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

class AppViewModel(
    private var db: IDataBase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState
    private var TAG: String = "MainActivityDebuging"

    @RequiresApi(Build.VERSION_CODES.O)
    fun setActualRutine(r: Rutine?) {
        _uiState.update { currentState ->
            currentState.copy(
                actualRutine = r,
                actualRuniteExercise = 0,
                remainingSets = r?.exercises?.get(0)?.sets ?: 0,
                actualTraining = Training(LocalDate.now(), mutableListOf(), r, false)
            )
        }
    }

    fun setNoRutineExerciseRutine(exerciseRutine: ExerciseRutine) {
        _uiState.update { currentState ->
            currentState.copy(
                noRutineExerciseRutine = exerciseRutine,
                remainingSets = exerciseRutine.sets
            )
        }
    }

    fun setEndedSet() : Int {
        _uiState.update { currentState ->
            currentState.copy(
                remainingSets = currentState.remainingSets - 1
            )
        }
        return _uiState.value.remainingSets
    }

    fun addRemainingSet() {
        _uiState.update { currentState ->
            currentState.copy(
                remainingSets = currentState.remainingSets + 1
            )
        }
    }

    fun setNextRutineExercise(t: TrainingExercise?) {
        if (t != null && t.sets.isNotEmpty())
            _uiState.value.actualTraining?.exercises?.add(t) ?: throw Error("The Training is not inicialiced")
        _uiState.update { currentState ->
            currentState.copy(
                noRutineExerciseRutine = null
            )
        }
        Log.d(TAG, "Actual Training: ${_uiState.value.actualTraining?.exercises?.joinToString(", ") { "${it.exercise.name} x${it.sets.size}" } ?: "Empty"}")
    }

    fun updateRutineToNextExercise() {
        _uiState.update { currentState ->
            currentState.copy(
                actualRuniteExercise = currentState.actualRuniteExercise + 1,
                remainingSets = currentState.actualRutine?.exercises?.get(currentState.actualRuniteExercise + 1)?.sets ?: 0
            )
        }
    }

    fun getNextExercise() : ExerciseRutine {
        if (uiState.value.noRutineExerciseRutine != null) {
            return uiState.value.noRutineExerciseRutine!!
        } else if (uiState.value.actualRutine != null) {
            val rutine: Rutine = uiState.value.actualRutine!!
            val exercisePosition: Int = uiState.value.actualRuniteExercise
            return rutine.exercises[exercisePosition]
        } else {
            throw Error("The rutine and the exercise temporal are not inicialiced")
        }
    }

    fun isLastRutineExercie() : Boolean {
        if (uiState.value.actualRutine == null) return true
        val exercisePosition: Int = uiState.value.actualRuniteExercise
        val rutine: Rutine = uiState.value.actualRutine!!
        return rutine.exercises.size == exercisePosition + 1
    }

    fun getRemainingSets(): Int {
        return _uiState.value.remainingSets
    }

    fun getActualTraining(): Training {
        return _uiState.value.actualTraining ?: throw Error("The training is null")
    }

    fun getActualRutine(): Rutine? {
        return _uiState.value.actualRutine
    }

    fun getMenuMesage(): (@Composable (onClick: () -> Unit) -> Unit)? {
        return _uiState.value.menuMessage
    }

    fun resetMenuMessage() {
        _uiState.update { currentState ->
            currentState.copy(
                menuMessage = null,
            )
        }
    }

    fun setMenuMessage(function: (@Composable (onClick: () -> Unit) -> Unit)?) {
        _uiState.update { currentState ->
            currentState.copy(
                menuMessage = function,
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getExerciseHistorical(e : Exercise) : List<TrainingExercise> {
        return db.getExerciseTrainings(e)
    }

    fun getAllRutines() : List<Rutine> {
        return db.getAllRutines()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getLastExerciseSet(e: Exercise): ExerciseSet? {
        var trainings: List<TrainingExercise> = db.getExerciseTrainings(e)
        var last = trainings.maxByOrNull { it.date ?: LocalDate.MIN }
        return last?.sets?.get(0)
        /// return null
    }

    fun saveTraining(t: Training) {
        db.saveTraining(t)
    }

    fun getMuscleExercises(m: MuscularGroup): List<Exercise> {
        return db.getExercisesByMuscle(m)
    }

}