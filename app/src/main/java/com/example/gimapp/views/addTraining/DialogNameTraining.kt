package com.example.gimapp.views.addTraining

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults.colors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.gimapp.ui.theme.GimAppTheme

@Composable
fun DialogNameTraining(
    onSaveTraining: (String) -> Unit,
    onSaveRutine: (String) -> Unit,
    onExit: () -> Unit
) {
    Dialog(onDismissRequest = { onExit() }) {
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
                text = "Selecciona un nombre",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color.White
                ),
                textAlign = TextAlign.Center
            )
            var text: String by remember { mutableStateOf("") }
            val focusRequester = remember { FocusRequester() }
            val focusManager = LocalFocusManager.current
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                colors = colors(
                    focusedContainerColor = MaterialTheme.colorScheme.secondary,
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondary,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done,
                    capitalization = KeyboardCapitalization.Sentences
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
            )
            Button(
                onClick = { onSaveTraining(text) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = if (text.equals("")) "Guardar como entrenamiento sin nombre" else "Guardar entrenamiento",
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = Color.White
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(vertical = 5.dp)
                )
            }
            Button(
                onClick = { if (text != "") onSaveRutine(text) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = if (text == "") Color.Gray else Color.White
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "Guardar como rutina",
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = if (text == "") Color.Gray else Color.White
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(vertical = 5.dp)
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewDialogNameTraining() {
    GimAppTheme {
        Spacer(modifier = Modifier.fillMaxSize())
        DialogNameTraining (
            { }, { }, { }
        )
    }
}