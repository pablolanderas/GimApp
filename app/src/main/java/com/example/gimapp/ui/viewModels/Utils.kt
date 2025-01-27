package com.example.gimapp.ui.viewModels

import com.example.gimapp.data.database.DataBase
import com.example.gimapp.domain.Exercise

suspend fun addModeToExercise(
    exercise: Exercise,
    mode: String,
    db: DataBase,
    trueCode: (()->Unit)? = null,
    falseCode:(()->Unit)? = null
) {
    exercise.mode = mode.lowercase().trim()
    if(db.getExerciseModeIdNullable(exercise) == null) {
        db.saveExercise(exercise)
        trueCode?.invoke()
    } else {
        falseCode?.invoke()
    }
}

suspend fun addExerciseLogic(
    exercise: Exercise,
    db: DataBase,
    trueCode: (()->Unit)? = null,
    falseCode:(()->Unit)? = null
) {
    if (db.existExercise(exercise.name)) {
        falseCode?.invoke()
    } else {
        db.saveExercise(exercise)
        trueCode?.invoke()
    }
}