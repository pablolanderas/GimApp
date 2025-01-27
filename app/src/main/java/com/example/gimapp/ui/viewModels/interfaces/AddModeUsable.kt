package com.example.gimapp.ui.viewModels.interfaces

import android.content.Context

interface AddModeUsable {

    fun closeAddMode()

    fun addModeToExercise(name: String, context: Context)

}