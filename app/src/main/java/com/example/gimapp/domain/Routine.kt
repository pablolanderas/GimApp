package com.example.gimapp.domain

import java.time.LocalDate

enum class MuscularGroup(
    val displayName: String
) {
    Chest("Pecho"),
    Shoulder("Hombro"),
    Back("Espalda"),
    Trapeze("Trapecio"),
    Biceps("Biceps"),
    Triceps("Triceps"),
    Leg("Pierna"),
    Twin("Gemelo"),
    Abs("Abdominales");

    fun getText(): String { return displayName }

}

data class Exercise(
    val name: String,
    var mode: String,
    val muscle: MuscularGroup = MuscularGroup.Chest,
    val imgURI: Int? = null
)

class ExerciseRoutine(
    var exercise: Exercise,
    val sets: Int,
    val minReps: Int,
    val maxReps: Int
) {
    fun isReal() : Boolean {
        return sets > 0 && minReps > 0 && maxReps > 0 && minReps <= maxReps
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true // Son la misma instancia
        if (other !is ExerciseRoutine) return false // No es del mismo tipo

        return this.exercise.name == other.exercise.name &&
                this.exercise.mode == other.exercise.mode &&
                this.sets == other.sets &&
                this.minReps == other.minReps &&
                this.maxReps == other.maxReps
    }

    override fun hashCode(): Int {
        return listOf(exercise.name, exercise.mode, sets, minReps, maxReps).hashCode()
    }
}

class Routine(
    val name: String,
    val exercises: MutableList<ExerciseRoutine>,
    var id: Long = 0
)

class ExerciseSet(
    val weight: Double,
    val reps: Int,
    val effort: Int
)

class TrainingExercise(
    val exercise: Exercise,
    val date: LocalDate?,
    val sets: MutableList<ExerciseSet>
) {
    fun getWeightAverage(): Double {
        return sets.sumOf { it.weight } / sets.size
    }
}
   
class Training(
    val date: LocalDate,
    val exercises: MutableList<TrainingExercise>,
    var routine: Routine?,
    var modifiedRoutine: Boolean,
    var id: Long = 0
)
