package com.example.gimapp.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gimapp.data.database.DataBase
import com.example.gimapp.domain.Exercise
import com.example.gimapp.domain.MuscularGroup
import com.example.gimapp.domain.Training
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ExercisesViewModel @Inject constructor(
    private val db: DataBase
) : ViewModel() {

    private val _muscularGroup = MutableLiveData<MuscularGroup?>()
    val muscularGroup: LiveData<MuscularGroup?> = _muscularGroup
    private val _exercisesOptions = MutableLiveData<List<Exercise>>()
    val exercisesOptions: LiveData<List<Exercise>> = _exercisesOptions

    fun updateMuscularGroup(m: MuscularGroup) {
        _muscularGroup.value = m
        viewModelScope.launch {
            _exercisesOptions.value = db.getExercisesByMuscle(m)
        }
    }

}