package com.example.gimapp.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gimapp.data.database.entities.ExerciseEntity

@Dao
interface ExerciseDao {

    @Query("SELECT * FROM exercise")
    suspend fun getAllExercises(): List<ExerciseEntity>

    @Query("SELECT * FROM exercise WHERE muscle = :group")
    suspend fun getAllExercisesFromMuscle(group: Int): List<ExerciseEntity>

    @Query("SELECT * FROM exercise WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): ExerciseEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exercise: ExerciseEntity): Long

}