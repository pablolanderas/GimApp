package com.example.gimapp

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.example.gimapp.data.AppUiState
import com.example.gimapp.domain.Exercise
import com.example.gimapp.domain.ExerciseRutine
import com.example.gimapp.domain.ExerciseSet
import com.example.gimapp.domain.Rutine
import com.example.gimapp.domain.Training
import com.example.gimapp.domain.TrainingExercise
import com.example.gimapp.domain.historialPressBanca
import com.example.gimapp.domain.rutinasPrueba
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

class AppViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState
    private var TAG: String = "MainActivityDebuging"

    @RequiresApi(Build.VERSION_CODES.O)
    fun setActualRutine(r: Rutine) {
        _uiState.update { currentState ->
            currentState.copy(
                actualRutine = r,
                actualRuniteExercise = 0,
                remainingSets = r.exercises[0].sets,
                actualTraining = Training(LocalDate.now(), mutableListOf())
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

    fun getRutineExercise() : ExerciseRutine {
        val exercisePosition: Int = uiState.value.actualRuniteExercise
        val rutine: Rutine = uiState.value.actualRutine ?: throw Error("The rutine is null")
        return rutine.exercises[exercisePosition]
    }

    fun isLastRutineExercie() : Boolean {
        val exercisePosition: Int = uiState.value.actualRuniteExercise
        val rutine: Rutine = uiState.value.actualRutine ?: throw Error("The rutine is null")
        return rutine.exercises.size == exercisePosition + 1
    }

    fun getRemainingSets(): Int {
        return _uiState.value.remainingSets
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getExerciseHistorical(e : Exercise) : List<TrainingExercise> {
        Log.d(TAG, "Called the history of ${e.name}")
        return historialPressBanca
    }

    fun getAllRutines() : List<Rutine> {
        return rutinasPrueba
    }

    fun getLastExerciseSet(e: Exercise): ExerciseSet? {
        return null
    }

}