package com.example.gimapp.ui.viewModels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gimapp.data.database.DataBase
import com.example.gimapp.domain.Exercise
import com.example.gimapp.domain.MuscularGroup
import com.example.gimapp.domain.TrainingExercise
import com.example.gimapp.ui.viewModels.interfaces.AddExerciseUsable
import com.example.gimapp.ui.viewModels.interfaces.AddModeUsable
import com.example.gimapp.ui.viewModels.managers.NavigateManager
import com.example.gimapp.ui.viewModels.managers.ToastManager
import com.example.gimapp.ui.views.GimScreens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ExercisesViewModel @Inject constructor(
    private val db: DataBase
) : ViewModel(), AddModeUsable, AddExerciseUsable {

    private val _muscularGroup = MutableLiveData<MuscularGroup?>()
    val muscularGroup: LiveData<MuscularGroup?> = _muscularGroup
    private val _exercisesOptions = MutableLiveData<List<Exercise>>()
    val exercisesOptions: LiveData<List<Exercise>> = _exercisesOptions
    private val _selectedExercise = MutableLiveData<Exercise>()
    val selectedExercise: LiveData<Exercise> = _selectedExercise
    private val _selectedExerciseModes = MutableLiveData<List<String>>()
    val selectedExerciseModes: LiveData<List<String>> = _selectedExerciseModes
    private val _visibleDialogInfo = MutableLiveData<Boolean>()
    val visibleDialogInfo: LiveData<Boolean> = _visibleDialogInfo
    private val _exerciseHistorical = MutableLiveData<List<TrainingExercise>>()
    val exerciseHistorical: LiveData<List<TrainingExercise>> = _exerciseHistorical
    private val _visibleDialogAddMode = MutableLiveData<Boolean>()
    val visibleDialogAddMode: LiveData<Boolean> = _visibleDialogAddMode
    private val _visibleDialogAddExercise = MutableLiveData<Boolean>()
    val visibleDialogAddExercise: LiveData<Boolean> = _visibleDialogAddExercise


    fun updateMuscularGroup(m: MuscularGroup) {
        _muscularGroup.value = m
        viewModelScope.launch {
            _exercisesOptions.value = db.getExercisesByMuscle(m)
        }
    }

    fun selectExercise(e: Exercise) {
        _selectedExercise.value = e
        viewModelScope.launch {
            _selectedExerciseModes.value = db.getExerciseModes(e)
            _selectedExerciseModes.value!!.forEach { Log.d("DEV", "VALE: $it") }
        }
        NavigateManager.navigateTo(GimScreens.ExerciseInfo)
    }

    fun showModeInfo(mode: String) {
        _selectedExercise.value!!.mode = mode
        viewModelScope.launch {
            _exerciseHistorical.value = db.getExerciseTrainings(_selectedExercise.value!!)
        }
        _visibleDialogInfo.value = true
    }

    fun closeDialogInfo() {
        _visibleDialogInfo.value = false
    }

    fun showAddMode() {
        _visibleDialogAddMode.value = true
    }

    override fun closeAddMode() {
        _visibleDialogAddMode.value = false
    }

    override fun addModeToExercise(name: String, context: Context) {
        viewModelScope.launch {
            addModeToExercise(
                exercise = selectedExercise.value!!,
                mode = name,
                db = db,
                trueCode = {
                    showToast("Se ha añadido el modo correctamente", context)
                    _selectedExerciseModes.value = _selectedExerciseModes.value!! + name
                    closeAddMode()
                },
                falseCode = {
                    showToast("Ese modo ya existe", context)
                }
            )
        }
    }

    fun openAddExercise() {
        _visibleDialogAddExercise.value = true
    }

    override fun closeAddExercise() {
        _visibleDialogAddExercise.value = false
    }

    override fun addExercise(exercise: Exercise, context: Context) {
        viewModelScope.launch {
            addExerciseLogic(
                exercise = exercise,
                db = db,
                trueCode = {
                    showToast("Se ha añadido correctamente", context)
                    _exercisesOptions.value = _exercisesOptions.value!! + exercise
                    closeAddExercise()
                },
                falseCode = {
                    showToast("El ejercicio que estas intentando añadir ya existe", context)
                }
            )
        }
    }

    private fun showToast(message: String, context: Context, length: Int = Toast.LENGTH_SHORT) {
        ToastManager.showToast(message, context, length)
    }

}