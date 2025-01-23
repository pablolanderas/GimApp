package com.example.gimapp.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gimapp.data.database.DataBase
import com.example.gimapp.domain.Training
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoricalViewModel @Inject constructor(
    private val db: DataBase
) : ViewModel() {

    private val _trainings = MutableLiveData<List<Training>>()
    val trainings: LiveData<List<Training>> = _trainings

    fun updateTrainings() {
        viewModelScope.launch {
            _trainings.value = db.getAllTrainings()
        }
    }

}