package com.example.gimapp.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.gimapp.domain.Exercise
import com.example.gimapp.domain.ExerciseRoutine
import com.example.gimapp.domain.ExerciseSet
import com.example.gimapp.domain.MuscularGroup
import com.example.gimapp.domain.Routine
import com.example.gimapp.domain.Training
import java.time.LocalDate

@Entity(tableName = "exercise")
data class ExerciseEntity(
    @PrimaryKey
    val name: String,
    val muscle: Int,
    val imgUri: Int? = null
) {
    fun toDomain(mode: String): Exercise {
        return Exercise(name, mode, MuscularGroup.entries[muscle], imgUri)
    }
    companion object {
        fun fromDomain(e: Exercise) : ExerciseEntity {
            return ExerciseEntity(name=e.name, muscle=e.muscle.ordinal, imgUri=e.imgURI)
        }
    }
}

@Entity(
    tableName = "mode",
    foreignKeys = [
        ForeignKey(
            entity = ExerciseEntity::class,
            parentColumns = ["name"],
            childColumns = ["exerciseFk"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ModeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val exerciseFk: String,
    val mode: String
)

@Entity(tableName = "routine")
class RoutineEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String
) {
    fun toDomain(): Routine {
        return Routine(name, mutableListOf(), id)
    }
    companion object {
        fun fromDomain(r: Routine) : RoutineEntity {
            return RoutineEntity(name=r.name, id=r.id)
        }
    }
}

@Entity(
    tableName = "exercise_routine",
    primaryKeys = ["position", "exerciseFk", "routineFk"],
    foreignKeys = [
        ForeignKey(
            entity = ModeEntity::class,
            parentColumns = ["id"],
            childColumns = ["exerciseFk"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = RoutineEntity::class,
            parentColumns = ["id"],
            childColumns = ["routineFk"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("exerciseFk"), Index("routineFk"), Index("position")]
)
data class ExerciseRoutineEntity(
    val position: Int,
    val exerciseFk: Long,
    val routineFk: Long,
    val sets: Int,
    val minReps: Int,
    val maxReps: Int
) {
    fun toDomain(e: Exercise): ExerciseRoutine {
        return ExerciseRoutine(e, sets, minReps, maxReps)
    }
    companion object {
        fun fromDomain(e: ExerciseRoutine, position: Int, routineFK: Long, modeId: Long) : ExerciseRoutineEntity {
            return ExerciseRoutineEntity(
                position = position,
                exerciseFk = modeId,
                routineFk = routineFK,
                sets = e.sets,
                minReps = e.minReps,
                maxReps = e.maxReps
            )
        }
    }
}

@Entity(tableName = "training")
data class TrainingEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: LocalDate,
    val routineId: Long?,
    val modifiedRoutine: Boolean
) {
    fun toDomain(r:Routine?): Training {
        return Training(date, mutableListOf(), r, modifiedRoutine, id)
    }
    companion object {
        fun fromDomain(t: Training) : TrainingEntity {
            return TrainingEntity(t.id, t.date, t.routine?.id, t.modifiedRoutine)
        }
    }
}

const val DEFAULT_EFFORT = 0

@Entity(
    tableName = "exercise_set",
    primaryKeys = ["position", "trainingFk"],
    foreignKeys = [
        ForeignKey(
            entity = TrainingEntity::class,
            parentColumns = ["id"],
            childColumns = ["trainingFk"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ModeEntity::class,
            parentColumns = ["id"],
            childColumns = ["exerciseFk"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("trainingFk"), Index("exerciseFk")]
)
data class ExerciseSetEntity(
    val position: Int,
    val trainingFk: Long,
    val exerciseFk: Long,
    val weight: Double,
    val reps: Int
) {
    fun toDomain(): ExerciseSet {
        return ExerciseSet(weight, reps, DEFAULT_EFFORT)
    }
    companion object {
        suspend fun fromDomain(t: Training, idGetter: suspend  (Exercise) -> Long) : List<ExerciseSetEntity> {
            val exerciseSets = mutableListOf<ExerciseSetEntity>()
            var count = 1
            t.exercises.forEach { e ->
                e.sets.forEach { s ->
                    exerciseSets.add(
                        ExerciseSetEntity(count++, t.id, idGetter(e.exercise), s.weight, s.reps)
                    )
                }
            }
            return exerciseSets
        }
    }
}

data class ExerciseTrainingDetailsMapper(
    val trainingFk: Long,
    val date: LocalDate,
    val weight: Double,
    val reps: Int
) {
    fun toDomain(): ExerciseSet {
        return ExerciseSet(weight, reps, DEFAULT_EFFORT)
    }
}

data class ExerciseWithModeMapper(
    val name: String,
    val mode: String,
    val muscle: Int,
    val imgUri: Int? = null,
) {
    fun toDomain(): Exercise {
        return ExerciseEntity(name, muscle, imgUri).toDomain(mode)
    }
}