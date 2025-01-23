CAMBIO DE ARQUITECTURA A MVVM

# Hecho
[x] Revisada la vista main
[x] Revisada la vista SelectRoutine
[x] Revisada la vista NextExercise
[x] Revisada la vista AddExerciseToTraining
[x] Revisada la vista OnSet
[x] Revisada la vista EndRoutine
[x] Revisada la vista DialogChangedRutine
[x] Revisada la vista DialogNameTraining
[x] Revisada la vista Historical
[ ] Revisada la vista SeeTraining

# TODO
- Arreglar decimales en pesos
- Arreglar peso cuando te dice el proximo ejercicio
- Esconder el teclado al aceptar el max de repeticiones en añadir ejercicio a entrenamiento
- Actualizar ejercicio
    - cambiar id, mejor el nombre
    - actualizar vistas para q solo muestre el nombre, y el modo lo seleccionas dentro
- Arreglar navegacion
- Hacer lo de subir o bajar peso
- Gestionar que pasa si añader un ejercicio al entrenamiento que ya ha hecho
   (sobre todo mirar que pasa si van seguidos en base de datos)
- Lazy load para el historial los entrenos viejos

# Enlaces
- video de la inyeccion de dependencias: https://www.youtube.com/watch?v=PBYnVFT2CI8&t=550s

# Base de datos

Exercise
| long: id | string: name | string: mode | int: muscle | int: imgUri |
PrimaryKey (id)

MuscularGroup -> Que se diferencie entre valores con un entero

Routine
| long: id | string: name |
PrimaryKey (id)

ExerciseRoutine
| int: position | int: exercise_fk | int routine_fk | int: sets | int: minReps | int: maxReps |
PrimaryKey (position, exercise_fk, routine_fk)

Training
| long: id | date: date | long: routineId | boolean: modifiedRoutine |
PrimaryKey (id)

ExerciseSet -> Quiero que la date se fije como la date del training_fk.date
| int: position | int training_fk | int exercise_fk | double: weight | int: reps |
PrimaryKey (position, training_fk)

# Entities

@Entity(tableName = "exercise")
data class ExerciseEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val mode: String,
    val muscle: Int,
    val imgUri: Int? = null
)

@Entity(tableName = "Routine")
data class RoutineEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String
)

@Entity(
    tableName = "ExerciseRoutine",
    primaryKeys = ["position", "exerciseFk", "routineFk"],
    foreignKeys = [
        ForeignKey(
            entity = ExerciseEntity::class,
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
    indices = [Index("exerciseFk"), Index("routineFk")]
)
data class ExerciseRoutineEntity(
    val position: Int,
    val exerciseFk: Long,
    val routineFk: Long,
    val sets: Int,
    val minReps: Int,
    val maxReps: Int
)

@Entity(tableName = "training")
data class TrainingEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: LocalDate,
    val routineId: Long?,
    val modifiedRoutine: Boolean
)

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
            entity = ExerciseEntity::class,
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
)
