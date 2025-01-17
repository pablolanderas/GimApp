package com.example.gimapp.data

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.gimapp.R
import com.example.gimapp.domain.Exercise
import com.example.gimapp.domain.ExerciseRutine
import com.example.gimapp.domain.ExerciseSet
import com.example.gimapp.domain.MuscularGroup
import com.example.gimapp.domain.Routine
import com.example.gimapp.domain.Training
import com.example.gimapp.domain.TrainingExercise
import java.time.LocalDate

class DatabBasev1: IDataBase {

    override fun saveRutine(r: Routine) {
        // TODO
    }

    override fun getAllRutines(): List<Routine> {
        return rutinasPrueba
    }

    override fun saveTraining(t: Training) {
        // TODO
    }

    override fun getAllTrainings(): List<Training> {
        return listOf(createSampleTraining(), createSampleTraining(), createSampleTraining(), createSampleTraining())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getExerciseTrainings(e: Exercise): List<TrainingExercise> {
        return historialPressBanca
    }

    override fun saveExercise(e: Exercise) {
        // TODO
    }

    override fun getExercisesByMuscle(m: MuscularGroup): List<Exercise> {
        return ejerciciosPrueba.filter { it.muscle == m }
    }
}

val rutinasPrueba = mutableListOf(
    Routine(
        name = "Pull",
        exercises = mutableListOf(
            ExerciseRutine(
                exercise = Exercise("press banca", "con banca", imgURI = R.drawable.press_banca),
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
    Routine(
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
    Routine(
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
        exercise = Exercise("press banca", "normal", imgURI =  R.drawable.press_banca),
        date = LocalDate.of(2024, 8, 13),
        sets = mutableListOf(
            ExerciseSet(weight = 70.0, reps = 10, effort = 2),
            ExerciseSet(weight = 70.0, reps = 10, effort = 2),
            ExerciseSet(weight = 70.0, reps = 10, effort = 2),
            ExerciseSet(weight = 70.0, reps = 10, effort = 2)
        )
    ),
    TrainingExercise(
        exercise = Exercise("press banca", "normal", imgURI = R.drawable.press_banca),
        date = LocalDate.of(2024, 8, 10),
        sets = mutableListOf(
            ExerciseSet(weight = 68.0, reps = 14, effort = 2),
            ExerciseSet(weight = 68.0, reps = 14, effort = 2),
            ExerciseSet(weight = 68.0, reps = 13, effort = 2),
            ExerciseSet(weight = 70.0, reps = 10, effort = 2)
        )
    )
)

val ejerciciosPrueba: List<Exercise> = listOf(
    Exercise("press banca", "normal", MuscularGroup.Chest),
    Exercise("sentadilla", "con barra", MuscularGroup.Leg),
    Exercise("peso muerto", "convencional", MuscularGroup.Leg),
    Exercise("press militar", "con barra", MuscularGroup.Shoulder),
    Exercise("dominadas", "con peso", MuscularGroup.Back),
    Exercise("remo con barra", "horizontal", MuscularGroup.Back),
    Exercise("curl de bíceps", "con barra", MuscularGroup.Biceps),
    Exercise("extensiones de tríceps", "con polea", MuscularGroup.Triceps),
    Exercise("press inclinado", "con mancuernas", MuscularGroup.Chest),
    Exercise("zancadas", "con barra", MuscularGroup.Leg),
    Exercise("elevaciones laterales", "con mancuernas", MuscularGroup.Shoulder),
    Exercise("abdominales", "en banco declinado", MuscularGroup.Chest),  // Se puede ajustar según lo que busques.
    Exercise("jalón al pecho", "en polea", MuscularGroup.Back),
    Exercise("remo en polea baja", "horizontal", MuscularGroup.Back),
    Exercise("hip thrust", "con barra", MuscularGroup.Leg),
    Exercise("press de pecho", "en máquina", MuscularGroup.Chest),
    Exercise("press de pierna", "en máquina", MuscularGroup.Leg),
    Exercise("peso muerto rumano", "con barra", MuscularGroup.Leg),
    Exercise("crunch abdominal", "con peso", MuscularGroup.Abs),  // Similar a abdominales, podría ser abdominal.
    Exercise("press Arnold", "con mancuernas", MuscularGroup.Shoulder),
    Exercise("press de pecho en banco plano", "con barra", MuscularGroup.Chest),
    Exercise("leg press", "en máquina", MuscularGroup.Leg),
    Exercise("remo con mancuernas", "inclinado", MuscularGroup.Back),
    Exercise("fondos en paralelas", "con peso", MuscularGroup.Triceps),
    Exercise("peso muerto sumo", "con barra", MuscularGroup.Leg),
    Exercise("press militar con mancuernas", "alterno", MuscularGroup.Shoulder),
    Exercise("dominadas con agarre amplio", "sin peso", MuscularGroup.Back),
    Exercise("curl martillo", "con mancuernas", MuscularGroup.Biceps),
    Exercise("extensiones de piernas", "en máquina", MuscularGroup.Leg),
    Exercise("pull-over", "con mancuerna", MuscularGroup.Chest),
    Exercise("remo en máquina", "sentado", MuscularGroup.Back),
    Exercise("flexiones de brazo", "en suelo", MuscularGroup.Chest),
    Exercise("zancadas caminando", "con mancuernas", MuscularGroup.Leg),
    Exercise("crunch inverso", "en banco plano", MuscularGroup.Abs),  // Aquí cambiamos el grupo muscular a Abs.
    Exercise("elevaciones frontales", "con mancuernas", MuscularGroup.Shoulder),
    Exercise("jalón con agarre estrecho", "en polea", MuscularGroup.Back),
    Exercise("sentadilla búlgara", "con mancuernas", MuscularGroup.Leg),
    Exercise("curl de piernas", "en máquina", MuscularGroup.Leg),
    Exercise("elevación de talones", "en máquina", MuscularGroup.Twin),  // Gemelos.
    Exercise("press de hombro", "en máquina", MuscularGroup.Shoulder),
    Exercise("push press", "con barra", MuscularGroup.Shoulder)
)

fun createSampleTraining(): Training {
    // Crear ejercicios base
    val exercise1 = Exercise(name = "Press de banca", mode = "Fuerza", muscle = MuscularGroup.Chest)
    val exercise2 = Exercise(name = "Sentadilla", mode = "Fuerza", muscle = MuscularGroup.Leg)
    val exercise3 = Exercise(name = "Curl de bíceps", mode = "Fuerza", muscle = MuscularGroup.Biceps)

    // Crear rutinas de ejercicios con repeticiones y series
    val exerciseRutine1 = ExerciseRutine(exercise = exercise1, sets = 3, minReps = 8, maxReps = 12)
    val exerciseRutine2 = ExerciseRutine(exercise = exercise2, sets = 4, minReps = 10, maxReps = 15)
    val exerciseRutine3 = ExerciseRutine(exercise = exercise3, sets = 3, minReps = 12, maxReps = 15)

    // Crear una rutina con los ejercicios
    val routine = Routine(
        name = "Rutina de fuerza",
        exercises = mutableListOf(exerciseRutine1, exerciseRutine2, exerciseRutine3)
    )

    // Crear sets de cada ejercicio realizado en el entrenamiento
    val exerciseSet1 = ExerciseSet(weight = 80.0, reps = 10, effort = 8)
    val exerciseSet2 = ExerciseSet(weight = 80.0, reps = 9, effort = 8)
    val exerciseSet3 = ExerciseSet(weight = 85.0, reps = 8, effort = 9)

    val exerciseSet4 = ExerciseSet(weight = 100.0, reps = 12, effort = 7)
    val exerciseSet5 = ExerciseSet(weight = 105.0, reps = 10, effort = 8)
    val exerciseSet6 = ExerciseSet(weight = 110.0, reps = 8, effort = 9)

    val exerciseSet7 = ExerciseSet(weight = 15.0, reps = 15, effort = 7)
    val exerciseSet8 = ExerciseSet(weight = 15.0, reps = 12, effort = 7)
    val exerciseSet9 = ExerciseSet(weight = 17.5, reps = 10, effort = 8)

    // Asociar sets a cada ejercicio del entrenamiento
    val trainingExercise1 = TrainingExercise(exercise = exercise1, date = LocalDate.now(), sets = mutableListOf(exerciseSet1, exerciseSet2, exerciseSet3))
    val trainingExercise2 = TrainingExercise(exercise = exercise2, date = LocalDate.now(), sets = mutableListOf(exerciseSet4, exerciseSet5, exerciseSet6))
    val trainingExercise3 = TrainingExercise(exercise = exercise3, date = LocalDate.now(), sets = mutableListOf(exerciseSet7, exerciseSet8, exerciseSet9))

    // Crear el entrenamiento final con los ejercicios realizados
    return Training(
        date = LocalDate.now(),
        exercises = mutableListOf(trainingExercise1, trainingExercise2, trainingExercise3),
        routine = routine,
        modifiedRutine = false
    )
}
