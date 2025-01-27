package com.example.gimapp.views.addTraining

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gimapp.ui.viewModels.TrainingViewModel
import com.example.gimapp.ui.views.components.Header
import com.example.gimapp.data.database.DataBase
import com.example.gimapp.data.database.daos.DaosDatabase_Impl
import com.example.gimapp.domain.ExerciseRoutine
import com.example.gimapp.domain.Routine
import com.example.gimapp.ui.theme.GimAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropListRutine(
    options: List<Routine>,
    selectedOption: Routine?,
    selected: Boolean,
    optionSelected: (Routine?) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ){
        Text(
            modifier = Modifier.padding(
                vertical = 10.dp
            ),
            text = "Rutina:",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color.White
            )
        )
        var expanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            modifier = Modifier
                .background(
                    color = Color(0x00000000),
                    shape = RoundedCornerShape(16.dp)
                ),
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = selectedOption?.name ?: if (!selected) "Seleccione una rutina" else "Entrenamiento libre",
                onValueChange = { },
                readOnly = true,
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White,
                    textAlign = TextAlign.Left
                ),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = "Dropdown icon",
                        tint = Color.White
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.primary,
                    unfocusedContainerColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = MaterialTheme.colorScheme.primary,
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor() // Ancla el menú al TextField
                    .background(
                        color = Color(0x00000000),
                        shape = RoundedCornerShape(16.dp)
                    )
            )

            // Crear el menú desplegable
            ExposedDropdownMenu(
                modifier = Modifier
                    .background(
                        color = Color(0x00FF0000),
                        shape = RoundedCornerShape(16.dp)
                    ),
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = option.name.replaceFirstChar { it.uppercase() },
                                textAlign = TextAlign.Start,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
                        onClick = {
                            optionSelected(option)
                            expanded = false
                        },
                    )
                }
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "--- Entrenamiento libre ---",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    onClick = {
                        optionSelected(null)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun ListExercisesRutine(routine: Routine?, modifier: Modifier = Modifier) {
    val ejercicios = routine?.exercises ?: emptyList<ExerciseRoutine>()
    Column(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier.padding(
                bottom = 10.dp
            ),
            text = "Ejercicios:",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color.White
            )
        )
        LazyColumn(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(10.dp)
                )
                .fillMaxSize()
        ){
            items(ejercicios) { ejercicio ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = 5.dp,
                            horizontal = 10.dp
                        )
                        .background(
                            color = MaterialTheme.colorScheme.secondary,
                            shape = RoundedCornerShape(10.dp)
                        )
                ) {
                    Text(
                        text = ejercicio.exercise.name.replaceFirstChar { it.uppercase() },
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "x${ejercicio.sets}",
                        modifier = Modifier
                            .padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SelectRoutine(viewModel: TrainingViewModel) {
    val selectedOption: Routine? by viewModel.routine.observeAsState(initial = null)
    val selected: Boolean by viewModel.startedRoutine.observeAsState(initial = false)
    val routines: List<Routine> by viewModel.routines.observeAsState(initial = emptyList())
    viewModel.loadAllRoutines()
    Header() {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp)
        ){
            Column (
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly
            ){
                val verticalMargin = 0.15f

                Spacer(modifier = Modifier.weight(verticalMargin))

                DropListRutine(
                    options = routines,
                    selectedOption = selectedOption,
                    selected = selected,
                    optionSelected = {
                        viewModel.setRoutine(it)
                        viewModel.setSelected()
                    }
                )

                if (selectedOption != null)
                    ListExercisesRutine(
                        selectedOption,
                        Modifier
                            .weight(1f)
                            .padding(vertical = 50.dp)
                    )
                else Spacer(modifier = Modifier.weight(1f))

                Button(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .align(Alignment.CenterHorizontally)
                        .background(
                            color = MaterialTheme.colorScheme.secondary,
                            shape = RoundedCornerShape(10.dp)
                        ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = Color.White,
                        disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                        disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                    ),
                    enabled = selected,
                    onClick = { if (selected) viewModel.startRoutine() }
                ){
                    Text(
                        text = "EMPEZAR",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Spacer(modifier = Modifier.weight(verticalMargin))
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showSystemUi = true)
@Composable
fun PreviewSelectRoutine() {
    GimAppTheme {
        SelectRoutine(TrainingViewModel(DataBase(DaosDatabase_Impl())))
    }
}