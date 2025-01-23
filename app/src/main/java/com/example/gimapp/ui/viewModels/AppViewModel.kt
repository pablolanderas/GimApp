package com.example.gimapp.ui.viewModels

import androidx.lifecycle.ViewModel
import com.example.gimapp.domain.Training

class AppViewModel() : ViewModel() {


    fun getAllTrainings(): List<Training> {
        return emptyList()
    }

    fun setTrainingWatching(t: Training) {

    }

}