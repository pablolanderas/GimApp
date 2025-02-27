package com.example.gimapp.ui.views.menu

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.gimapp.data.database.DataBase
import com.example.gimapp.data.database.daos.DaosDatabase_Impl
import com.example.gimapp.ui.theme.GimAppTheme
import com.example.gimapp.ui.viewModels.ExercisesViewModel
import com.example.gimapp.ui.viewModels.TrainingViewModel
import com.example.gimapp.ui.views.exercises.DialogModeInfo

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DialogGoToOldTraining(
    viewModel: TrainingViewModel
) {
    Dialog(onDismissRequest = { viewModel.closeDialogTrainingStartedBefore() }) {
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
                text = "Se ha encontrado un ejercicio que dejate a medias",
                style = MaterialTheme.typography.titleMedium
            )
            Button(
                onClick = { viewModel.restoreTrainingDataPreferences() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .padding(vertical = 10.dp)
            ) {
                Text(
                    text = "Volver al entrenamiento",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Button(
                onClick = { viewModel.closeDialogTrainingStartedBefore() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth(0.85f)
            ) {
                Text(
                    text = "Ignorar",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showSystemUi = false)
@Composable
fun PreviewDialogGoToOldTraining() {
    GimAppTheme {
        DialogGoToOldTraining(TrainingViewModel(DataBase(DaosDatabase_Impl())))
    }
}