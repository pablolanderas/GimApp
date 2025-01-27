package com.example.gimapp.ui.viewModels.interfaces

import android.content.Context
import com.example.gimapp.domain.Exercise

interface AddExerciseUsable {

    fun closeAddExercise()

    fun addExercise(exercise: Exercise, context: Context)

}