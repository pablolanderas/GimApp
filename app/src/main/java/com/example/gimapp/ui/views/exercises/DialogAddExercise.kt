package com.example.gimapp.ui.views.exercises

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.gimapp.data.database.DataBase
import com.example.gimapp.data.database.daos.DaosDatabase_Impl
import com.example.gimapp.domain.Exercise
import com.example.gimapp.domain.MuscularGroup
import com.example.gimapp.ui.theme.GimAppTheme
import com.example.gimapp.ui.viewModels.interfaces.AddModeUsable
import com.example.gimapp.ui.viewModels.ExercisesViewModel
import com.example.gimapp.ui.viewModels.interfaces.AddExerciseUsable
import com.example.gimapp.ui.views.components.DropListMuscularGroup

@Composable
fun TextIndicator(
    title: String,
    name: String,
    onChange: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 10.dp,
                top = 15.dp
            ),
        textAlign = TextAlign.Start
    )
    TextField(
        value = name,
        onValueChange = onChange,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.secondary,
            unfocusedContainerColor = MaterialTheme.colorScheme.secondary,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() }
        ),
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            color = Color.White
        ),
        modifier = Modifier
            .padding(10.dp)
    )
}

@Composable
fun MuscleIndicator(muscle: MuscularGroup?, onSelected: (MuscularGroup)->Unit) {
    Text(
        text = "Grupo muscular",
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 10.dp,
                top = 15.dp
            ),
        textAlign = TextAlign.Start
    )
    DropListMuscularGroup(
        selectedOption = muscle,
        optionSelected = onSelected,
        modifier = Modifier
            .padding(10.dp),
        color = MaterialTheme.colorScheme.secondary
    )
}

@Composable
fun DialogAddExercise(
    viewModel: AddExerciseUsable
) {

    var name: String by remember { mutableStateOf("") }
    var mode: String by remember { mutableStateOf("") }
    var muscle: MuscularGroup? by remember { mutableStateOf(null) }
    val context = LocalContext.current

    Dialog(onDismissRequest = { viewModel.closeAddExercise() } ) {
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
                text = "Nuevo ejercicio",
                style = MaterialTheme.typography.titleLarge
            )

            TextIndicator("Nombre", name) { name = it }

            MuscleIndicator(muscle) { muscle = it }

            TextIndicator("Modo", mode) { mode = it }

            Button(
                onClick = { viewModel.addExercise(Exercise(name.lowercase().trim(), mode.lowercase().trim(), muscle!!), context) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 10.dp,
                        vertical = 15.dp
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = Color.White,
                    disabledContentColor = Color.Gray
                ),
                shape = RoundedCornerShape(10.dp),
                enabled = name.isNotEmpty() && muscle != null && mode.isNotEmpty()
            ) {
                Text(
                    text = "Añadir",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Preview(showSystemUi = false)
@Composable
fun PreviewDialogAddExercise() {
    GimAppTheme {
        DialogAddExercise(ExercisesViewModel(DataBase(DaosDatabase_Impl())))
    }
}