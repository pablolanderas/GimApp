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

class Exercise(
    val name: String,
    val mode: String,
    val muscle: MuscularGroup = MuscularGroup.Chest,
    val imgURI: Int? = null
)

class ExerciseRutine(
    val exercise: Exercise,
    val sets: Int,
    val minReps: Int,
    val maxReps: Int
) {
    fun isReal() : Boolean {
        return sets > 0 && minReps > 0 && maxReps > 0 && minReps <= maxReps
    }
}

class Routine(
    val name: String,
    val exercises: MutableList<ExerciseRutine>
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
)
   
class Training(
    val date: LocalDate,
    val exercises: MutableList<TrainingExercise>,
    val routine: Routine?,
    val modifiedRutine: Boolean
)
