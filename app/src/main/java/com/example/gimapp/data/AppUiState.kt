package com.example.gimapp.data

import com.example.gimapp.domain.Rutine
import com.example.gimapp.domain.Training
import com.example.gimapp.domain.TrainingExercise

data class AppUiState (
    val actualRutine : Rutine? = null,
    val actualRuniteExercise : Int = 0,
    val remainingSets : Int = 0,
    val actualTraining: Training? = null,
    val actualTrainingExcercise: TrainingExercise? = null
)