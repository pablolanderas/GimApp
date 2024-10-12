package com.example.gimapp.pantallas

import android.annotation.SuppressLint
import android.icu.text.DecimalFormat
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gimapp.R
import com.example.gimapp.componentes.Recubrimiento
import com.example.gimapp.dominio.EjercicioRutina
import com.example.gimapp.dominio.EntrenamientoEjercicio
import com.example.gimapp.dominio.Rutina
import com.example.gimapp.dominio.historialPressBanca
import com.example.gimapp.dominio.rutinasPrueba
import com.example.gimapp.ui.theme.GimAppTheme
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun BarraInfo(
    titulo: String,
    estiloTitulo: TextStyle = TextStyle(
        color = MaterialTheme.colorScheme.primary,
        fontSize = MaterialTheme.typography.bodyMedium.fontSize
    ),
    valor: String,
    estiloValor: TextStyle = TextStyle(
        color = Color.White,
        fontSize = MaterialTheme.typography.bodyMedium.fontSize
    ),
    modifier: Modifier,
    content: @Composable RowScope.() -> Unit = {}
) {
    Row(modifier = modifier){
        Text(
            text = titulo,
            style = estiloTitulo
        )
        Spacer(modifier = Modifier.weight(1F))
        content()
        Text(
            text = valor,
            style = estiloValor
        )
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun InfoEjercicio(
    ejercicioRutina: EjercicioRutina,
    peso: Double,
    subirPeso: Boolean,
    bajarPeso: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(15.dp)
            )
    ){
        Text(
            text = ejercicioRutina.ejercicio.nombre.replaceFirstChar { it.uppercase() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            textAlign = TextAlign.Center,
            style = TextStyle(
                color = Color.White,
                fontSize = MaterialTheme.typography.titleLarge.fontSize
            )
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.secondary,
                    shape = RoundedCornerShape(15.dp)
                )
        ){
            BarraInfo(
                titulo = "Modo:",
                valor = ejercicioRutina.ejercicio.modo.replaceFirstChar { it.uppercase() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 10.dp,
                        start = 20.dp,
                        end = 20.dp,
                    )
            )
            BarraInfo(
                titulo = "Series:",
                valor = "x${ejercicioRutina.series}",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 10.dp,
                        start = 20.dp,
                        end = 20.dp,
                    )
            )
            BarraInfo(
                titulo = "Repeticiones:",
                valor = "${ejercicioRutina.minRepeticiones}-${ejercicioRutina.maxRepeticiones}",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 10.dp,
                        start = 20.dp,
                        end = 20.dp,
                    )
            )
            BarraInfo(
                titulo = "Peso:",
                valor = "${DecimalFormat("#.##").format(peso)} kg",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 10.dp,
                        start = 20.dp,
                        end = 20.dp,
                        bottom = 10.dp
                    )
            ) {
                if (subirPeso)
                    Image(
                        painter = painterResource(id = R.drawable.flecha_arriba),
                        contentDescription = null
                    )
                if (bajarPeso)
                    Image(
                        painter = painterResource(id = R.drawable.flecha_abajo),
                        contentDescription = null
                    )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HistorialEjercicio(historial: List<EntrenamientoEjercicio>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)
            .background(
                MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(15.dp)
            )
    ) {
        items(historial) { entrenamiento ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(15.dp)
                    )
            ){
                entrenamiento.fecha?.let {
                    Text(
                        text = it.format(
                            DateTimeFormatter.ofPattern(
                                "d 'de' MMMM 'de' yyyy",
                                Locale("es", "ES")
                            )
                        ),
                        style = TextStyle(color = Color.White),
                        modifier = Modifier
                            .padding(horizontal = 15.dp, vertical = 15.dp)
                    )
                    var peso = DecimalFormat("#.##").format(entrenamiento.series.first().peso)
                    val pesos = entrenamiento.series.map {it.peso}
                    if (pesos.distinct().size != 1)
                        peso = pesos.joinToString(separator = ", ") { DecimalFormat("#.##").format(it) }
                    BarraInfo(
                        titulo = "Peso:",
                        estiloTitulo = TextStyle(
                            color = MaterialTheme.colorScheme.secondary,
                            fontSize = MaterialTheme.typography.bodySmall.fontSize
                        ),
                        valor = "${peso} kg",
                        estiloValor = TextStyle(
                            color = Color.White,
                            fontSize = MaterialTheme.typography.bodySmall.fontSize
                        ),
                        modifier = Modifier.padding(start = 15.dp, end = 15.dp, bottom = 7.dp)
                    )
                    BarraInfo(
                        titulo = "Repeticiones:",
                        estiloTitulo = TextStyle(
                            color = MaterialTheme.colorScheme.secondary,
                            fontSize = MaterialTheme.typography.bodySmall.fontSize
                        ),
                        valor = "${entrenamiento.series.map { it.repeticiones }.joinToString(separator = ", ")}",
                        estiloValor = TextStyle(
                            color = Color.White,
                            fontSize = MaterialTheme.typography.bodySmall.fontSize
                        ),
                        modifier = Modifier.padding(start = 15.dp, end = 15.dp, bottom = 12.dp)
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NextEjercicio() {
    val rutinas = rutinasPrueba
    var selectedOption: Rutina? by remember { mutableStateOf(null) }
    Recubrimiento(
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 30.dp)
        ){
            Text(
                text = "Siguiente:",
                color = Color(0xFFFFFFFF),
                style = TextStyle(
                    fontSize = MaterialTheme.typography.titleMedium.fontSize
                ),
                modifier = Modifier.padding(top = 25.dp, bottom = 15.dp)

            )
            InfoEjercicio(
                ejercicioRutina = rutinasPrueba[0].ejercicios[0],
                peso = 70.0,
                subirPeso = true,
                bajarPeso = false)
            Text(
                text = "Historial:",
                color = Color(0xFFFFFFFF),
                style = TextStyle(
                    fontSize = MaterialTheme.typography.titleMedium.fontSize
                ),
                modifier = Modifier.padding(top = 25.dp, bottom = 15.dp)
            )
            HistorialEjercicio(historialPressBanca)
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)) {
                Spacer(modifier = Modifier.weight(1F))
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(15.dp)
                        )
                ) {
                    Text(text = "EMPEZAR", style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    ))
                }
                Spacer(modifier = Modifier.weight(1F))
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 25.dp)) {
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .weight(0.2f)
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.tertiary,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .height(35.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent, // Fondo transparente
                        contentColor = Color.White // Color del texto
                    )

                ) {
                    Text(
                        text = "Otro",
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                Spacer(modifier = Modifier.weight(0.3f))
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .weight(0.2f)
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.tertiary,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .height(35.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent, // Fondo transparente
                        contentColor = Color.White // Color del texto
                    )

                ) {
                    Text(
                        text = "Saltar",
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showSystemUi = true)
@Composable
fun PrevieNextEjercicio() {
    GimAppTheme {
        NextEjercicio()
    }
}