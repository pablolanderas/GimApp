package com.example.gimapp.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gimapp.components.Header
import com.example.gimapp.domain.ExerciseRutine
import com.example.gimapp.domain.Rutine
import com.example.gimapp.ui.theme.GimAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropListRutine(
    options: List<Rutine>,
    selectedOption: Rutine?,
    optionSelected: (Rutine) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ){
        Text(
            modifier = Modifier.padding(
                vertical = 10.dp
            ),
            text = "Rutina:",
            style = TextStyle(
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                color = Color(0xFFFFFFFF)
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
                value = selectedOption?.name ?: "Seleccione una rutina",
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
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option.name) },
                        onClick = {
                            optionSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ListExercisesRutine(rutine: Rutine?) {
    val ejercicios = rutine?.exercises ?: emptyList<ExerciseRutine>()
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.padding(
                vertical = 10.dp
            ),
            text = "Ejercicios:",
            style = TextStyle(
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                color = Color(0xFFFFFFFF)
            )
        )
        LazyColumn(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(10.dp)
                )
                .fillMaxHeight(0.5f)
                .fillMaxWidth()
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
                        style = TextStyle(
                            color = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "x${ejercicio.sets}",
                        modifier = Modifier
                            .padding(16.dp),
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun SelectRutine(
    onRutineSelected : (Rutine) -> Unit,
    rutines: List<Rutine>
) {
    var selectedOption: Rutine? by remember { mutableStateOf(null) }
    Header() {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp)
        ){
            Column (
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly
            ){
                DropListRutine(
                    rutines,
                    selectedOption,
                    { selectedOption = it }
                )

                ListExercisesRutine(selectedOption)

                Button(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .align(Alignment.CenterHorizontally)
                        .background(
                            color = MaterialTheme.colorScheme.secondary,
                            shape = RoundedCornerShape(10.dp)
                        ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    ),
                    onClick = { selectedOption?.let { onRutineSelected(it) } }
                ){
                    Text(
                        text = "EMPEZAR",
                        style = TextStyle(
                            color = Color(0xFFFFFFFF),
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewSelectRutina() {
    GimAppTheme {
        SelectRutine({}, emptyList())
    }
}