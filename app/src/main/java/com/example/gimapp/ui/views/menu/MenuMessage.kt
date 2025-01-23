package com.example.gimapp.views.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.gimapp.ui.theme.GimAppTheme


@Composable
fun MenuMessage(
    message: String,
    exit: () -> Unit
) {
    Dialog(onDismissRequest = exit) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(10.dp)
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = exit,
                    modifier = Modifier
                        .padding(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent, // Fondo transparente
                        contentColor = Color.White // Color del texto
                    )
                ) {
                    Text(
                        text = "x",
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                            fontWeight = FontWeight.Bold
                    ))
                }

            }

            Text(
                text = message,
                style = TextStyle(
                    color = Color.Black,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    fontWeight = FontWeight.Normal
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(vertical = 5.dp)
                    .padding(bottom = 40.dp)
            )

        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewMenuMessage() {
    GimAppTheme {
        Spacer(modifier = Modifier.fillMaxSize())
        MenuMessage(
            "Mensaje de prueba\nCon varias lineas",
            {}
        )
    }
}