package com.example.gimapp.views.addTraining

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gimapp.components.Header
import com.example.gimapp.domain.Rutine
import com.example.gimapp.ui.theme.GimAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropListMuscularGroup(
    options: List<Rutine?>,
    selectedOption: Rutine?,
    selected: Boolean,
    optionSelected: (Rutine?) -> Unit
) {
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
                    text = {
                        androidx.compose.material3.Text(
                            text = option?.name ?: "--- Entrenamiento libre ---",
                            textAlign = if (option==null) TextAlign.Center else TextAlign.Start,  // Centra el texto dentro de la DropdownMenuItem
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
fun AddExerciseToTraining() {
    Header(
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {
        Text(
            text = "Selecciona el ejercicio",
            color = Color.White,
            style = TextStyle(
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewAddExerciseToTraining() {
    GimAppTheme {
        AddExerciseToTraining()
    }
}