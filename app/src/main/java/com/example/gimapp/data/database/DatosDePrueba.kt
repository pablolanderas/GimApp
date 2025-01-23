package com.example.gimapp.data.database

import com.example.gimapp.domain.Exercise
import com.example.gimapp.domain.MuscularGroup

val ejercicisosDePrueba = listOf(
    Exercise(name = "press banca", mode = "en banco", muscle = MuscularGroup.Chest),
    Exercise(name = "aperturas", mode = "en máquina", muscle = MuscularGroup.Chest),
    Exercise(name = "press inclinado", mode = "con mancuernas", muscle = MuscularGroup.Chest),
    Exercise(name = "elevaciones laterales", mode = "con mancuernas", muscle = MuscularGroup.Shoulder),
    Exercise(name = "press militar", mode = "en máquina", muscle = MuscularGroup.Shoulder),
    Exercise(name = "extensión de triceps", mode = "con polea doble", muscle = MuscularGroup.Triceps),
    Exercise(name = "extensión de triceps", mode = "con polea doble y dropset", muscle = MuscularGroup.Triceps),
    Exercise(name = "jalón al pecho", mode = "con poleas", muscle = MuscularGroup.Back),
    Exercise(name = "remo", mode = "con poleas", muscle = MuscularGroup.Back),
    Exercise(name = "pull over", mode = "con poleas", muscle = MuscularGroup.Back),
    Exercise(name = "curl de biceps", mode = "con poleas", muscle = MuscularGroup.Biceps)
)