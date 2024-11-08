package com.example.gimapp.domain

import android.os.Build
import androidx.annotation.RequiresApi

class DatabBasev1: IDataBase {

    override fun saveRutine(r: Rutine) {
        // TODO
    }

    override fun getAllRutines(): List<Rutine> {
        return rutinasPrueba
    }

    override fun saveTraining(t: Training) {
        // TODO
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getExerciseTrainings(e: Exercise): List<TrainingExercise> {
        return historialPressBanca
    }

    override fun saveExercise(e: Exercise) {
        // TODO
    }

    override fun getExercisesByMuscle(m: MuscularGroup): List<Exercise> {
        return ejerciciosPrueba.filter { it.muscle == m }
    }
}