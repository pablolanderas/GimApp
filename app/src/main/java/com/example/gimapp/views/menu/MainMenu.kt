package com.example.gimapp.views.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gimapp.components.Header
import com.example.gimapp.ui.theme.GimAppTheme

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

@Composable
fun MainMenu(
    onNewTrainClicked: () -> Unit,
    onHistoricalClicked: () -> Unit
) {
    Header(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MenuButton(text = "Nuevo entrenamiento", onClicked = onNewTrainClicked)
        MenuButton(text = "Historial", onClicked = onHistoricalClicked)
        MenuButton(text = "Rutinas")
        MenuButton(text = "Ejercicios")
        MenuButton(text = "Ajustes")
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewPaginaInicio() {
    GimAppTheme {
        MainMenu({}, {})
    }
}