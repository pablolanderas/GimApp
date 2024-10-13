package com.example.gimapp.domain

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate

class Exercise(
    val name: String,
    val mode: String
) {
}

class ExerciseRutine(
    val exercise: Exercise,
    val sets: Int,
    val minReps: Int,
    val maxReps: Int
)

class Rutine(
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
    val exercises: MutableList<TrainingExercise>
)

val rutinasPrueba = mutableListOf(
    Rutine(
        name = "Pull",
        exercises = mutableListOf(
            ExerciseRutine(
                exercise = Exercise("press banca", "con banca"),
                sets = 1,
                minReps = 8,
                maxReps = 10
            ),
            ExerciseRutine(
                exercise = Exercise("press inclinado", "con mancuernas"),
                sets = 4,
                minReps = 8,
                maxReps = 10
            ),
            ExerciseRutine(
                exercise = Exercise("aperturas", "con maquina"),
                sets = 4,
                minReps = 8,
                maxReps = 10
            ),
            ExerciseRutine(
                exercise = Exercise("press militar", "con maquina"),
                sets = 4,
                minReps = 8,
                maxReps = 10
            ),
            ExerciseRutine(
                    exercise = Exercise("crull de triceps", "con polea doble"),
            sets = 4,
            minReps = 8,
            maxReps = 10
        )
        )
    ),
    Rutine(
        name = "Push",
        exercises = mutableListOf(
            ExerciseRutine(
                exercise = Exercise("jalon al pecho", "con poleas y barra"),
                sets = 4,
                minReps = 8,
                maxReps = 10
            ),
            ExerciseRutine(
                exercise = Exercise("contracciones", "con maquina"),
                sets = 4,
                minReps = 8,
                maxReps = 10
            ),
            ExerciseRutine(
                exercise = Exercise("crull de biceps", "con mancuernas"),
                sets = 4,
                minReps = 8,
                maxReps = 10
            )
        )
    ),
    Rutine(
        name = "Pierna",
        exercises = mutableListOf(
            ExerciseRutine(
                exercise = Exercise("sentadilla", "con barra"),
                sets = 4,
                minReps = 8,
                maxReps = 10
            ),
            ExerciseRutine(
                exercise = Exercise("extensiones de cuadriceps", "con maquina"),
                sets = 4,
                minReps = 8,
                maxReps = 10
            ),
            ExerciseRutine(
                exercise = Exercise("peso muerto", "con barra"),
                sets = 4,
                minReps = 8,
                maxReps = 10
            ),
            ExerciseRutine(
                exercise = Exercise("hip thrust", "con barra"),
                sets = 4,
                minReps = 8,
                maxReps = 10
            ),
            ExerciseRutine(
                exercise = Exercise("gemelo", "con maquina"),
                sets = 4,
                minReps = 8,
                maxReps = 10
            )
        )
    )
)

@RequiresApi(Build.VERSION_CODES.O)
val historialPressBanca: List<TrainingExercise> = listOf(
    TrainingExercise(
        exercise = Exercise("press banca", "normal"),
        date = LocalDate.of(2024, 8, 13),
        sets = mutableListOf(
            ExerciseSet(weight = 70.0, reps = 10, effort = 2),
            ExerciseSet(weight = 70.0, reps = 10, effort = 2),
            ExerciseSet(weight = 70.0, reps = 10, effort = 2),
            ExerciseSet(weight = 70.0, reps = 10, effort = 2)
        )
    ),
    TrainingExercise(
        exercise = Exercise("press banca", "normal"),
        date = LocalDate.of(2024, 8, 10),
        sets = mutableListOf(
            ExerciseSet(weight = 68.0, reps = 14, effort = 2),
            ExerciseSet(weight = 68.0, reps = 14, effort = 2),
            ExerciseSet(weight = 68.0, reps = 13, effort = 2),
            ExerciseSet(weight = 70.0, reps = 10, effort = 2)
        )
    )
)