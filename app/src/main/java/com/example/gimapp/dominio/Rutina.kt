package com.example.gimapp.dominio

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate

class Ejercicio(
    val nombre: String,
    val modo: String
) {
}

class EjercicioRutina(
    val ejercicio: Ejercicio,
    val series: Int,
    val minRepeticiones: Int,
    val maxRepeticiones: Int
)

class Rutina(
    val nombre: String,
    val ejercicios: List<EjercicioRutina>
)

class Serie(
    val peso: Double,
    val repeticiones: Int,
    val esfuerzo: Int
)

class EntrenamientoEjercicio(
    val fecha: LocalDate?,
    val series: List<Serie>
)

public val rutinasPrueba = listOf(
    Rutina(
        nombre = "Pull",
        ejercicios = listOf(
            EjercicioRutina(
                ejercicio = Ejercicio("press banca", "con banca"),
                series = 4,
                minRepeticiones = 8,
                maxRepeticiones = 10
            ),
            EjercicioRutina(
                ejercicio = Ejercicio("press inclinado", "con mancuernas"),
                series = 4,
                minRepeticiones = 8,
                maxRepeticiones = 10
            ),
            EjercicioRutina(
                ejercicio = Ejercicio("aperturas", "con maquina"),
                series = 4,
                minRepeticiones = 8,
                maxRepeticiones = 10
            ),
            EjercicioRutina(
                ejercicio = Ejercicio("press militar", "con maquina"),
                series = 4,
                minRepeticiones = 8,
                maxRepeticiones = 10
            ),
            EjercicioRutina(
                    ejercicio = Ejercicio("crull de triceps", "con polea doble"),
            series = 4,
            minRepeticiones = 8,
            maxRepeticiones = 10
        )
        )
    ),
    Rutina(
        nombre = "Push",
        ejercicios = listOf(
            EjercicioRutina(
                ejercicio = Ejercicio("jalon al pecho", "con poleas y barra"),
                series = 4,
                minRepeticiones = 8,
                maxRepeticiones = 10
            ),
            EjercicioRutina(
                ejercicio = Ejercicio("contracciones", "con maquina"),
                series = 4,
                minRepeticiones = 8,
                maxRepeticiones = 10
            ),
            EjercicioRutina(
                ejercicio = Ejercicio("crull de biceps", "con mancuernas"),
                series = 4,
                minRepeticiones = 8,
                maxRepeticiones = 10
            )
        )
    ),
    Rutina(
        nombre = "Pierna",
        ejercicios = listOf(
            EjercicioRutina(
                ejercicio = Ejercicio("sentadilla", "con barra"),
                series = 4,
                minRepeticiones = 8,
                maxRepeticiones = 10
            ),
            EjercicioRutina(
                ejercicio = Ejercicio("extensiones de cuadriceps", "con maquina"),
                series = 4,
                minRepeticiones = 8,
                maxRepeticiones = 10
            ),
            EjercicioRutina(
                ejercicio = Ejercicio("peso muerto", "con barra"),
                series = 4,
                minRepeticiones = 8,
                maxRepeticiones = 10
            ),
            EjercicioRutina(
                ejercicio = Ejercicio("hip thrust", "con barra"),
                series = 4,
                minRepeticiones = 8,
                maxRepeticiones = 10
            ),
            EjercicioRutina(
                ejercicio = Ejercicio("gemelo", "con maquina"),
                series = 4,
                minRepeticiones = 8,
                maxRepeticiones = 10
            )
        )
    )
)

@RequiresApi(Build.VERSION_CODES.O)
public val historialPressBanca: List<EntrenamientoEjercicio> = listOf(
    EntrenamientoEjercicio(
        fecha = LocalDate.of(2024, 8, 13),
        series = listOf(
            Serie(peso = 70.0, repeticiones = 10, esfuerzo = 2),
            Serie(peso = 70.0, repeticiones = 10, esfuerzo = 2),
            Serie(peso = 70.0, repeticiones = 10, esfuerzo = 2),
            Serie(peso = 70.0, repeticiones = 10, esfuerzo = 2)
        )
    ),
    EntrenamientoEjercicio(
        fecha = LocalDate.of(2024, 8, 10),
        series = listOf(
            Serie(peso = 68.0, repeticiones = 14, esfuerzo = 2),
            Serie(peso = 68.0, repeticiones = 14, esfuerzo = 2),
            Serie(peso = 68.0, repeticiones = 13, esfuerzo = 2),
            Serie(peso = 70.0, repeticiones = 10, esfuerzo = 2)
        )
    )
)