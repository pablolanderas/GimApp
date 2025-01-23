package com.example.gimapp.views.addTraining

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.gimapp.ui.viewModels.TrainingViewModel
import com.example.gimapp.data.database.DataBase
import com.example.gimapp.data.database.daos.DaosDatabase_Impl
import com.example.gimapp.ui.theme.GimAppTheme

@Composable
fun DialogChangedRoutine(viewModel: TrainingViewModel) {
    Dialog(onDismissRequest = { viewModel.closeChangedRutineDialog() }) {
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
                text = "Se han detectado cambios en la rutina",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color.White
                ),
                textAlign = TextAlign.Center
            )
            Text(
                text = "¿Quieres guardar una modificación de la que tenías, crear una nueva o guardar sin registrar una rutina?",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(vertical = 20.dp)
            )
            Row {
                Button(
                    onClick = { viewModel.saveRoutineModification() },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 5.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "Modificación",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp)
                    )
                }
                Button(
                    onClick = { viewModel.tryToCreateNewRoutine() },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 5.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "Nueva",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp)
                    )
                }
            }
            Button(
                onClick = { viewModel.saveTrainWithoutRoutine() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "Guardar sin registrar",
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = Color.White
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
fun PreviewDialogChangedRutine() {
    GimAppTheme {
        Spacer(modifier = Modifier.fillMaxSize())
        DialogChangedRoutine(TrainingViewModel(DataBase(DaosDatabase_Impl())))
    }
}