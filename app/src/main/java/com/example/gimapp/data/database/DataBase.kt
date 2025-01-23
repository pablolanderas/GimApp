package com.example.gimapp.data.database

import android.util.Log
import com.example.gimapp.data.database.daos.DaosDatabase
import com.example.gimapp.data.database.entities.ExerciseEntity
import com.example.gimapp.data.database.entities.ExerciseRoutineEntity
import com.example.gimapp.data.database.entities.ExerciseSetEntity
import com.example.gimapp.data.database.entities.RoutineEntity
import com.example.gimapp.data.database.entities.TrainingEntity
import com.example.gimapp.domain.Exercise
import com.example.gimapp.domain.MuscularGroup
import com.example.gimapp.domain.Routine
import com.example.gimapp.domain.Training
import com.example.gimapp.domain.TrainingExercise
import javax.inject.Inject

class DataBase @Inject constructor(
    private val daos: DaosDatabase
) {

    suspend fun saveRoutine(r: Routine) {
        val id: Long = daos.getRoutineDao().insertRoutine(RoutineEntity.fromDomain(r))
        r.exercises.forEachIndexed { index, exerciseRoutine ->
            daos.getRoutineDao().insertExerciseRoutineEntity(
                ExerciseRoutineEntity.fromDomain(exerciseRoutine, index, id)
            )
        }
        r.id = id
    }

    suspend fun getAllRoutines(): List<Routine> {
        val routines = daos.getRoutineDao().getAllRoutines().map { it.toDomain() }
        if (routines.isEmpty()) return routines
        val exercises = mutableMapOf<Long, Exercise>()
        var r = routines.first()
        daos.getRoutineDao().getAllExerciseRoutine().forEach {
            if (it.routineFk != r.id) {
                r = routines.find { r -> r.id == it.routineFk }!!
            }

            updateExerciseDictIfNotContains(it.exerciseFk, exercises)

            r.exercises.add(it.toDomain(exercises[it.exerciseFk]!!))
        }
        return routines
    }

    private suspend fun getRoutineById(id: Long, exercises: MutableMap<Long, Exercise>): Routine {
        Log.d("DEV", "Entrado con id:$id")
        val routine = daos.getRoutineDao().getRoutineById(id).toDomain()
        daos.getRoutineDao().getAllExerciseRoutine(id).forEach {
            updateExerciseDictIfNotContains(it.exerciseFk, exercises)
            routine.exercises.add(it.toDomain(exercises[it.exerciseFk]!!))
        }
        return routine
    }

    suspend fun saveTraining(t: Training) {
        t.id = daos.getTrainingDao().insertTraining(TrainingEntity.fromDomain(t))
        ExerciseSetEntity.fromDomain(t).forEach {
            daos.getTrainingDao().insertExerciseSet(it)
        }
    }

    suspend fun getAllTrainings(): List<Training> {
        val trainingsEntities = daos.getTrainingDao().getAllTrainings()
        if (trainingsEntities.isEmpty()) return emptyList()
        val exercises = mutableMapOf<Long, Exercise>()
        val routines = mutableMapOf<Long?, Routine?>(0L to null)

        var actTrainingId: Long = -1
        var actExerciseId: Long = -1
        val trainings = mutableListOf<Training>()
        daos.getTrainingDao().getAllExerciseSet().forEach {

            updateExerciseDictIfNotContains(it.exerciseFk, exercises)

            if (actTrainingId != it.trainingFk) {
                val te = trainingsEntities.find { r -> r.id == it.trainingFk }!!
                if (te.routineId !in routines) {
                    Log.d("DEV", "Antes con id:${te.routineId}")
                    routines[te.routineId] = getRoutineById(te.routineId!!, exercises)
                }
                trainings.add(
                    Training(te.date, mutableListOf(), routines[te.routineId], te.modifiedRoutine, te.id)
                )
                actTrainingId = it.trainingFk
                actExerciseId = -1
            }

            if (actExerciseId != it.exerciseFk) {
                trainings.last().exercises.add(
                    TrainingExercise(exercises[it.exerciseFk]!!, null, mutableListOf())
                )
                actExerciseId = it.exerciseFk
            }

            trainings.last().exercises.last().sets.add(it.toDomain())
        }
        return trainings
    }

    suspend fun getExerciseTrainings(e: Exercise): List<TrainingExercise> {
        val trainings: MutableList<TrainingExercise> = mutableListOf()
        var actTrainingId: Long = -1
        daos.getTrainingDao().getExerciseTrainingDetails(e.id).forEach {
            if (it.trainingFk != actTrainingId) {
                trainings.add(TrainingExercise(e, it.date, mutableListOf()))
                actTrainingId = it.trainingFk
            }
            trainings.last().sets.add(it.toDomain())
        }
        return trainings
    }

    suspend fun saveExercise(e: Exercise) {
        val id = daos.getExerciseDao().insert(ExerciseEntity.fromDomain(e))
        e.id = id
    }

    suspend fun getExercisesByMuscle(m: MuscularGroup): List<Exercise> {
        return daos.getExerciseDao().getAllExercisesFromMuscle(m.ordinal).map {
            it.toDomain()
        }
    }

    private suspend fun updateExerciseDictIfNotContains(id: Long, map: MutableMap<Long, Exercise>) {
        if (id !in map) {
            map[id] =
                daos.getExerciseDao().getById(id).toDomain()
        }
    }

}