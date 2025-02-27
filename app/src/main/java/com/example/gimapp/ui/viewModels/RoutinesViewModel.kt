package com.example.gimapp.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gimapp.data.database.DataBase
import com.example.gimapp.domain.Exercise
import com.example.gimapp.domain.MuscularGroup
import com.example.gimapp.domain.Routine
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel
class RoutinesViewModel @Inject constructor(
    private val db: DataBase
) : ViewModel() {

    private val _routines = MutableLiveData<List<Routine>>()
    val routines: LiveData<List<Routine>> = _routines
    private val _selectedRoutine = MutableLiveData<Routine?>()
    val selectedRoutine: LiveData<Routine?> = _selectedRoutine
    private val _seeAddRoutine = MutableLiveData<Boolean>()
    val seeAddRoutine: LiveData<Boolean> = _seeAddRoutine
    // DialogAddRoutine
    private val _actualMuscularGroup = MutableLiveData<MuscularGroup>()
    val actualMuscularGroup: LiveData<MuscularGroup> = _actualMuscularGroup
    private val _exerciseOptions = MutableLiveData<List<Exercise>>()
    val exerciseOptions: LiveData<List<Exercise>> = _exerciseOptions
    // DialogSetModeAndData
    private val _exerciseSelected = MutableLiveData<Exercise>()
    val exerciseSelected: LiveData<Exercise> = _exerciseSelected
    private val _modesOptions = MutableLiveData<List<String>>()
    val modesOptions: LiveData<List<String>> = _modesOptions

    fun launchRoutines() {
        viewModelScope.launch {
            _routines.value = db.getAllRoutines()
        }
    }

    fun selectRoutine(r: Routine) {
        _selectedRoutine.value = r
    }

    fun closeAddRoutine() {
        _seeAddRoutine.value = false
    }

    fun openAddRoutine() {
        _seeAddRoutine.value = true
    }

    fun updateMuscularExercises(muscle: MuscularGroup) {
        _actualMuscularGroup.value = muscle
        viewModelScope.launch {
            _exerciseOptions.value = db.getExercisesByMuscle(muscle)
        }
    }

    fun selectExercise(exercise: Exercise) {
        _exerciseSelected.value = exercise
        viewModelScope.launch {
            _modesOptions.value = db.getExerciseModes(exercise)
        }
    }

}
