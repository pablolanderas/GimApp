package com.example.gimapp.ui.views.routine

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.gimapp.domain.Exercise
import com.example.gimapp.domain.MuscularGroup
import com.example.gimapp.ui.theme.GimAppTheme
import com.example.gimapp.ui.views.components.ExercisesDisplay

@Composable
fun DialogAddExerciseToRoutine(
    muscularGroup: MuscularGroup?,
    actualOptions: List<Exercise>,
    updateOptions: (MuscularGroup) -> Unit,
    onSelectedExercise: (Exercise) -> Unit,
    onAddExercise: () -> Unit,
    onCloseDialog: () -> Unit
) {

    Dialog(onDismissRequest = onCloseDialog ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(.7f)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = "Selecciona ejercicio",
                style = MaterialTheme.typography.titleMedium
            )
            ExercisesDisplay(
                muscularGroup = muscularGroup,
                actualOptions = actualOptions,
                onChangedMuscularGroup = {
                    updateOptions(it)
                },
                onSelectedExercise = onSelectedExercise,
                onAddExercise = onAddExercise,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(30.dp)
            )
        }
    }
}

@Preview(showSystemUi = false)
@Composable
fun PreviewDialogAddExerciseToRoutine() {
    GimAppTheme {
        DialogAddExerciseToRoutine(MuscularGroup.Chest, emptyList(),{},{},{},{})
    }
}