package com.example.gimapp.data

import com.example.gimapp.domain.Exercise
import com.example.gimapp.domain.MuscularGroup
import com.example.gimapp.domain.Routine
import com.example.gimapp.domain.Training
import com.example.gimapp.domain.TrainingExercise

interface IDataBase {

    fun saveRutine(r: Routine)

    fun getAllRutines(): List<Routine>

    fun saveTraining(t: Training)

    fun getAllTrainings(): List<Training>

    fun getExerciseTrainings(e: Exercise): List<TrainingExercise>

    fun saveExercise(e: Exercise)

    fun getExercisesByMuscle(m: MuscularGroup): List<Exercise>

}