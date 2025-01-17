package com.example.gimapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gimapp.data.DatabBasev1
import com.example.gimapp.data.IDataBase
import com.example.gimapp.domain.Exercise
import com.example.gimapp.domain.ExerciseRutine
import com.example.gimapp.domain.MuscularGroup
import com.example.gimapp.domain.Routine
import com.example.gimapp.domain.TrainingExercise
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

val NULABLE_ROUTINE_EXERCISE = ExerciseRutine(Exercise("", "", MuscularGroup.Abs), -1, -1, -1)

@HiltViewModel
class TrainViewModel @Inject constructor() : ViewModel() {

    // Cambiar por inyeccion de dependencias //
    private val db: IDataBase = DatabBasev1() // Esto se definira en el constructor()
    ///////////////////////////////////////////

    private val _routine = MutableLiveData<Routine?>()
    val routine: LiveData<Routine?> = _routine
    private val _actualRoutineExerciseRoutineIndex = MutableLiveData<Int>()
    private val _actualExericseRoutine = MutableLiveData<ExerciseRutine?>()
    // null quiere decir que no quedan mas ejericicos


    fun setRoutine(r: Routine?) {
        _routine.value = r
        _actualRoutineExerciseRoutineIndex.value = 0
        _actualExericseRoutine.value = r?.exercises?.get(0)
    }
    fun getActualExerciseRoutine(): ExerciseRutine? {
        return _actualExericseRoutine.value
    }
    fun getActualExerciseRoutineNoNullable(): ExerciseRutine {
        return _actualExericseRoutine.value
            ?: NULABLE_ROUTINE_EXERCISE
    }
    fun setNextActualExerciseRoutine() {
        if (_actualExericseRoutine.value == actualExerciseRoutineByIndex() ) {
            if (actualRoutineExerciseRoutineIndexIsLast()) {
                _actualExericseRoutine.value = null
            } else {
                _actualRoutineExerciseRoutineIndex.value = _actualRoutineExerciseRoutineIndex.value!! + 1
                _actualExericseRoutine.value = actualExerciseRoutineByIndex()
            }
        } else {
            if (actualRoutineExerciseRoutineIndexIsLast()) {
                _actualExericseRoutine.value = null
            } else {
                _actualExericseRoutine.value = actualExerciseRoutineByIndex()
            }
        }
    }
    fun setOtherActualExerciseRoutine(e: ExerciseRutine) {
        if (actualRoutineExerciseRoutineIndexIsLast()) {
            _actualRoutineExerciseRoutineIndex.value = -1
        }
        _actualExericseRoutine.value = e
    }

    fun skipActualExerciseRoutine(onOtherExercise: () -> Unit, onEndRoutine: () -> Unit) {
        setNextActualExerciseRoutine()
        if (_actualExericseRoutine.value == null) {
            onEndRoutine()
        } else {
            onOtherExercise()
        }
    }

    fun startActualExerciseRoutine(onStartExercise: () -> Unit) {
        // Initialize the data
    }

    fun getAllRoutines(): List<Routine> {
        return db.getAllRutines()
    }

    fun getExerciseHistorical(e: Exercise): List<TrainingExercise> {
        return db.getExerciseTrainings(e)
    }

    // Functions to improve readability
    private fun actualExerciseRoutineByIndex(): ExerciseRutine {
        return _routine.value?.exercises?.get(_actualRoutineExerciseRoutineIndex.value ?: -1)
            ?: throw  IllegalStateException("ExerciseRoutine no está inicializado")
    }
    private fun actualRoutineExerciseRoutineIndexIsLast(): Boolean {
        /*
        Si el indice es -1, significa que ya se han añadido todos los ejercicios de la rutina
        y se estan añadiendo ejercios extra al final
         */
        return _actualRoutineExerciseRoutineIndex.value == -1
                || (((_actualRoutineExerciseRoutineIndex.value ?: -5) + 1 )
                ?: -1) == (_routine.value?.exercises?.size ?: 0)
    }

}