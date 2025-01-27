package com.example.gimapp.ui.views.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.gimapp.domain.Exercise
import com.example.gimapp.domain.MuscularGroup
import com.example.gimapp.ui.theme.GimAppTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropListMuscularGroup(
    modifier: Modifier,
    selectedOption: MuscularGroup?,
    optionSelected: (MuscularGroup) -> Unit,
    color: Color = MaterialTheme.colorScheme.primary
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
                focusedContainerColor = color,
                unfocusedContainerColor = color,
                disabledContainerColor = color,
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
                            style = MaterialTheme.typography.bodyMedium,
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
        items(options) { exercise ->
            Button(
                onClick = { selected(exercise) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                ),
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
            ) {
                Text(
                    text = exercise.name.replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.bodyMedium.copy(
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
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}


@Composable
fun ExercisesDisplay(
    muscularGroup: MuscularGroup?,
    actualOptions: List<Exercise>,
    onChangedMuscularGroup: (MuscularGroup) -> Unit,
    onSelectedExercise: (Exercise) -> Unit,
    onAddExercise: () -> Unit,
    modifier: Modifier = Modifier,
    centralPadding: Dp = 20.dp
) {

    Column(modifier) {
        DropListMuscularGroup(
            selectedOption = muscularGroup,
            optionSelected = {
                onChangedMuscularGroup(it)
            },
            modifier = Modifier
        )
        val exercisesModifier = Modifier
            .fillMaxHeight()
            .padding(top = centralPadding)

        AnimatedVisibility(muscularGroup != null) {
            ExerciseSelector(
                options = actualOptions,
                showAddExercise = true,
                selected = onSelectedExercise,
                onAddExercise = onAddExercise,
                modifier = exercisesModifier
            )
        }
//        else {
//                Spacer(modifier = exercisesModifier)
//        }
    }

}


@Preview(showSystemUi = true)
@Composable
fun PreviewExercisesDisplay() {
    GimAppTheme {
        ExercisesDisplay(null,emptyList(),{},{},{})
    }
}