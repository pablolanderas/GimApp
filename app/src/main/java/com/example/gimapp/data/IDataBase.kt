package com.example.gimapp.data

import com.example.gimapp.domain.Exercise
import com.example.gimapp.domain.MuscularGroup
import com.example.gimapp.domain.Rutine
import com.example.gimapp.domain.Training
import com.example.gimapp.domain.TrainingExercise

interface IDataBase {

    fun saveRutine(r: Rutine)

    fun getAllRutines(): List<Rutine>

    fun saveTraining(t: Training)

    fun getExerciseTrainings(e: Exercise): List<TrainingExercise>

    fun saveExercise(e: Exercise)

    fun getExercisesByMuscle(m: MuscularGroup): List<Exercise>

}