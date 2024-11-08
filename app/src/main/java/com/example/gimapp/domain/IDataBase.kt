package com.example.gimapp.domain

interface IDataBase {

    fun saveRutine(r: Rutine)

    fun getAllRutines(): List<Rutine>

    fun saveTraining(t: Training)

    fun getExerciseTrainings(e: Exercise): List<TrainingExercise>

    fun saveExercise(e: Exercise)

    fun getExercisesByMuscle(m: MuscularGroup): List<Exercise>

}