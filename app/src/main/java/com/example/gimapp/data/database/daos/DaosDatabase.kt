package com.example.gimapp.data.database.daos

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.gimapp.data.database.entities.Converters
import com.example.gimapp.data.database.entities.ExerciseEntity
import com.example.gimapp.data.database.entities.ExerciseRoutineEntity
import com.example.gimapp.data.database.entities.ExerciseSetEntity
import com.example.gimapp.data.database.entities.RoutineEntity
import com.example.gimapp.data.database.entities.TrainingEntity

@Database(entities = [
        ExerciseEntity::class,
        RoutineEntity::class,
        ExerciseRoutineEntity::class,
        TrainingEntity::class,
        ExerciseSetEntity::class
    ],
    version = 1)
@TypeConverters(Converters::class)
abstract class DaosDatabase: RoomDatabase() {

    abstract fun getExerciseDao(): ExerciseDao
    abstract fun getRoutineDao(): RoutineDao
    abstract fun getTrainingDao(): TrainingDao

}