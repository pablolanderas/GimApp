package com.example.gimapp.views.addTraining

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gimapp.components.Header
import com.example.gimapp.domain.Exercise
import com.example.gimapp.domain.MuscularGroup
import com.example.gimapp.domain.ejerciciosPrueba
import com.example.gimapp.ui.theme.GimAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropListMuscularGroup(
    modifier: Modifier,
    selectedOption: MuscularGroup?,
    optionSelected: (MuscularGroup) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        modifier = modifier
            .background(
                color = Color(0x00000000),
                shape = RoundedCornerShape(16.dp)
            ),
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = selectedOption?.getText() ?: "Seleccion un grupo muscular",
            onValueChange = { },
            readOnly = true,
            textStyle = TextStyle(
                color = Color.White
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
            MuscularGroup.values().forEach { option ->
                DropdownMenuItem(
                    text = {
                        androidx.compose.material3.Text(
                            text = option.getText(),  // Centra el texto dentro de la DropdownMenuItem
                            modifier = Modifier.fillMaxWidth()  // Asegura que el texto ocupe todo el ancho disponible
                        )
                    },
                    onClick = {
                        optionSelected(option)
                        expanded = false
                    },
                )
            }
        }
    }
}

@Composable
fun ExerciseSelector(
    modifier: Modifier = Modifier,
    selection: Exercise?,
    options: List<Exercise>,
    showAddExercise: Boolean,
    selected: (Exercise) -> Unit,
    onAddExercise: () -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(15.dp)
            )
            .padding(10.dp)
    ) {
        var InteralPadding = 0.dp
        items(options) { exercise ->
            val isSelected = exercise == selection
            Button(
                onClick = { selected(exercise) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (!isSelected) MaterialTheme.colorScheme.secondary else Color.Gray
                ),
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
            ) {
                Text(
                    text = exercise.name.replaceFirstChar { it.uppercase() },
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        color = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                )
            }
        }
        if (showAddExercise) {
            item {
                Button(
                    onClick = onAddExercise,
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "+",
                        color = Color.White,
                        style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

@Composable
fun AddExerciseToTraining(
    onContinue: (Exercise) -> Unit,
    getExercises: (MuscularGroup) -> List<Exercise>,
    onAddExercise: () -> Unit
) {
    Header(
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        var musucarGroup: MuscularGroup? by remember { mutableStateOf(null) }
        var exercise: Exercise? by remember { mutableStateOf(null) }
        var exerciseList = musucarGroup?.let { getExercises(it) } ?: emptyList()
        Text(
            text = "Selecciona el ejercicio",
            color = Color.White,
            style = TextStyle(
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp)
        )
        DropListMuscularGroup(
            modifier = Modifier
                .padding(
                    horizontal = 30.dp,
                    vertical = 30.dp
                ),
            selectedOption = musucarGroup,
            optionSelected = {
                musucarGroup = it
                exercise = null
            },
        )
        ExerciseSelector(
            modifier = Modifier
                .padding(
                    horizontal = 30.dp
                )
                .weight(1f),
            selection = exercise,
            options = exerciseList,
            showAddExercise = musucarGroup != null,
            selected = { exercise = it },
            onAddExercise = onAddExercise
        )
        Button(
            onClick = { exercise?.let { onContinue(it) } },
            shape = RoundedCornerShape(20), // Hace el botón redondo
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
            modifier = Modifier
                .padding(top = 20.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = "Continuar",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium
            )
        }
        Spacer(modifier = Modifier.height(30.dp))
    }
}


@Preview(showSystemUi = true)
@Composable
fun PreviewAddExerciseToTraining() {
    GimAppTheme {
        AddExerciseToTraining(
            onContinue = {},
            getExercises = {ejerciciosPrueba},
            onAddExercise = {}
        )
    }
}