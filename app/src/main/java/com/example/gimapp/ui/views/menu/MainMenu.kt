package com.example.gimapp.views.menu

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gimapp.ui.viewModels.TrainingViewModel
import com.example.gimapp.ui.views.components.Header
import com.example.gimapp.data.database.DataBase
import com.example.gimapp.data.database.daos.DaosDatabase_Impl
import com.example.gimapp.ui.theme.GimAppTheme
import com.example.gimapp.ui.viewModels.managers.NavigateManager
import com.example.gimapp.ui.views.GimScreens
import com.example.gimapp.ui.views.menu.DialogGoToOldTraining

@Composable
fun MenuButton(
    modifier: Modifier = Modifier,
    text:String,
    onClicked: () -> Unit = {}
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = 50.dp
            )
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(bottomStart = 16.dp, topEnd = 16.dp)
            ),
        onClick = onClicked
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium.copy(
                color = Color.White
            )
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainMenu(viewModel: TrainingViewModel) {

    val context = LocalContext.current
    val showDialogTrainingStarted by viewModel.trainingStartedBefore.observeAsState(false)

    LaunchedEffect(Unit) { viewModel.checkTrainingDataPreferences() }

    if (showDialogTrainingStarted) DialogGoToOldTraining(viewModel)

    Header(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MenuButton(text = "Nuevo entrenamiento") { viewModel.startNewTraining() }
        MenuButton(text = "Historial") { NavigateManager.navigateTo(GimScreens.Historical) }
        MenuButton(text = "Rutinas") { NavigateManager.navigateTo(GimScreens.SeeRoutines) }
        MenuButton(text = "Ejercicios") { NavigateManager.navigateTo(GimScreens.SeeExercises) }
        MenuButton(text = "Ajustes" ) {  }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showSystemUi = true)
@Composable
fun PreviewPaginaInicio() {
    GimAppTheme {
        MainMenu(TrainingViewModel(DataBase(DaosDatabase_Impl())))
    }
}