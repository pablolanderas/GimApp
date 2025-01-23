package com.example.gimapp.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gimapp.data.database.entities.ExerciseEntity
import com.example.gimapp.data.database.entities.ExerciseWithModeMapper
import com.example.gimapp.data.database.entities.ModeEntity

@Dao
interface ExerciseDao {

    @Query("SELECT * FROM exercise")
    suspend fun getAllExercises(): List<ExerciseEntity>

    @Query("SELECT * FROM exercise WHERE muscle = :group")
    suspend fun getAllExercisesFromMuscle(group: Int): List<ExerciseEntity>

    @Query("SELECT * FROM exercise WHERE name = :name LIMIT 1")
    suspend fun getByName(name: String): ExerciseEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercise(exercise: ExerciseEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMode(mode: ModeEntity): Long

    @Query("SELECT m.id FROM mode m WHERE m.exerciseFk = :exercise AND m.mode = :mode")
    suspend fun getModeId(exercise: String, mode: String): Long

    @Query("""
        SELECT e.name, m.mode, e.muscle, e.imgUri
        FROM mode m
        JOIN exercise e ON m.exerciseFk = e.name
        WHERE m.id = :id
    """)
    suspend fun getExerciseByModeId(id: Long): ExerciseWithModeMapper

    @Query("SELECT m.mode FROM mode m WHERE m.exerciseFk = :exercise")
    suspend fun getExerciseModes(exercise: String): List<String>

}