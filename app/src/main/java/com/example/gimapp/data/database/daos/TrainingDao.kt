package com.example.gimapp.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gimapp.data.database.entities.ExerciseSetEntity
import com.example.gimapp.data.database.entities.ExerciseTrainingDetails
import com.example.gimapp.data.database.entities.TrainingEntity

@Dao
interface TrainingDao {

        @Query("""
            SELECT es.trainingFk, t.date, es.weight, es.reps
            FROM exercise_set es
            JOIN training t ON es.trainingFk = t.id
            WHERE es.exerciseFk = :exerciseFk
            ORDER BY t.date DESC, es.trainingFk DESC, es.position ASC
        """)
        suspend fun getExerciseTrainingDetails(exerciseFk: Long): List<ExerciseTrainingDetails>

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertTraining(t: TrainingEntity): Long

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertExerciseSet(e: ExerciseSetEntity)

        @Query("SELECT * FROM training ORDER BY date DESC")
        suspend fun getAllTrainings(): List<TrainingEntity>

        @Query("SELECT * FROM exercise_set ORDER BY trainingFk DESC, position")
        suspend fun getAllExerciseSet(): List<ExerciseSetEntity>

}