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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.gimapp.data.database.DataBase
import com.example.gimapp.data.database.daos.DaosDatabase_Impl
import com.example.gimapp.ui.theme.GimAppTheme
import com.example.gimapp.ui.viewModels.interfaces.AddModeUsable
import com.example.gimapp.ui.viewModels.ExercisesViewModel

@Composable
fun DialogAddMode(
    viewModel: AddModeUsable
) {

    var name: String by remember { mutableStateOf("") }
    val context = LocalContext.current

    Dialog(onDismissRequest = { viewModel.closeAddMode() } ) {
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
                text = "Nuevo modo",
                style = MaterialTheme.typography.titleLarge
            )
            val focusManager = LocalFocusManager.current
            TextField(
                value = name,
                onValueChange = { name = it },
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
            Button(
                onClick = { viewModel.addModeToExercise(name, context) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = Color.White,
                    disabledContentColor = Color.Gray
                ),
                shape = RoundedCornerShape(10.dp),
                enabled = name.isNotEmpty()
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
fun PreviewDialogAddMode() {
    GimAppTheme {
        DialogAddMode(ExercisesViewModel(DataBase(DaosDatabase_Impl())))
    }
}