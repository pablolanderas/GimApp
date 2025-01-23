package com.example.gimapp

import com.example.gimapp.ui.GimAppController
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.example.gimapp.ui.theme.GimAppTheme
import com.example.gimapp.ui.viewModels.ExercisesViewModel
import com.example.gimapp.ui.viewModels.HistoricalViewModel
import com.example.gimapp.ui.viewModels.TrainingViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val trainingViewModel: TrainingViewModel by viewModels<TrainingViewModel>()
    private val historicalViewModel: HistoricalViewModel by viewModels<HistoricalViewModel>()
    private val exercisesViewModel: ExercisesViewModel by viewModels<ExercisesViewModel>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val controller = GimAppController(
            trainingViewModel,
            historicalViewModel,
            exercisesViewModel
        )

        setContent {
            GimAppTheme {
                controller.StartApp()
            }
        }
    }

}


