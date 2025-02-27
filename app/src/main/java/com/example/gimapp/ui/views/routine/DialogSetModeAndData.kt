package com.example.gimapp.ui.views.routine

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.gimapp.domain.Exercise
import com.example.gimapp.domain.ExerciseRoutine
import com.example.gimapp.views.addTraining.DataAdder
import com.example.gimapp.views.addTraining.ModeSelector
import com.example.gimapp.views.addTraining.RepsSelector

@Composable
fun DialogSetModeAndData(
    exercise: Exercise,
    modes: List<String>,
    onExit: () -> Unit,
    onAddModeToExercise: () -> Unit,
    onConfirm: (ExerciseRoutine) -> Unit
) {
    val focusMinRep = remember { FocusRequester() }
    val focusMaxRep = remember { FocusRequester() }
    var numSets: Int? by remember { mutableStateOf(null) }
    var minReps: Int? by remember { mutableStateOf(null) }
    var maxReps: Int? by remember { mutableStateOf(null) }
    var selected: String by remember { mutableStateOf("") }
    Dialog(onDismissRequest = onExit) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Añadiendo ${exercise.name.replaceFirstChar { it.uppercase() } }",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            ModeSelector(
                selectedOption = selected,
                options = modes,
                optionSelected = { selected = it },
                newModeSelected = onAddModeToExercise,
                modifier = Modifier
                    .padding(top = 20.dp)
            )
            DataAdder(
                description = "Número de series:",
                setValue = { numSets = it},
                onNext = { focusMinRep.requestFocus() },
                modifier = Modifier
                    .padding(top = 20.dp)
            )
            RepsSelector(
                setMinReps = { minReps = it },
                setMaxReps = { maxReps = it },
                minRepsFocusRequester = focusMinRep,
                maxRepsFocusRequester = focusMaxRep,
                modifier = Modifier
                    .padding(top = 20.dp)
            )
            val context: Context = LocalContext.current
            Button(
                onClick = {
                    if (numSets != null && minReps != null && maxReps != null && selected != "")
                        onConfirm(ExerciseRoutine(exercise.copy(mode = selected), numSets!!, minReps!!, maxReps!!))
                },
                shape = RoundedCornerShape(20), // Hace el botón redondo
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                modifier = Modifier
                    .padding(top = 20.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Añadir",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White,
                    )
                )
            }
        }
    }
}