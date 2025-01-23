package com.example.gimapp.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gimapp.data.database.entities.ExerciseRoutineEntity
import com.example.gimapp.data.database.entities.RoutineEntity

@Dao
interface RoutineDao {

    @Query("SELECT * FROM routine")
    suspend fun getAllRoutines(): List<RoutineEntity>

    @Query("SELECT * FROM routine WHERE id = :id")
    suspend fun getRoutineById(id: Long): RoutineEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutine(exercise: RoutineEntity): Long

    @Query("SELECT * FROM exercise_routine ORDER BY routineFk DESC, position")
    suspend fun getAllExerciseRoutine(): List<ExerciseRoutineEntity>

    @Query("SELECT * FROM exercise_routine WHERE routineFk = :routineFk ORDER BY position")
    suspend fun getAllExerciseRoutine(routineFk: Long): List<ExerciseRoutineEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExerciseRoutineEntity(exercise: ExerciseRoutineEntity)

}