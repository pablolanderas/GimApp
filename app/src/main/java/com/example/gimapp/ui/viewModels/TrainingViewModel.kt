package com.example.gimapp.ui.viewModels

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gimapp.data.database.DataBase
import com.example.gimapp.data.database.preferences.PreferencesManager
import com.example.gimapp.domain.Exercise
import com.example.gimapp.domain.ExerciseRoutine
import com.example.gimapp.domain.ExerciseSet
import com.example.gimapp.domain.MuscularGroup
import com.example.gimapp.domain.Routine
import com.example.gimapp.domain.Training
import com.example.gimapp.domain.TrainingExercise
import com.example.gimapp.ui.viewModels.interfaces.AddExerciseUsable
import com.example.gimapp.ui.viewModels.interfaces.AddModeUsable
import com.example.gimapp.ui.viewModels.managers.NavigateManager
import com.example.gimapp.ui.viewModels.managers.ToastManager
import com.example.gimapp.ui.views.GimScreens
import com.example.gimapp.ui.views.addTraining.AddSetState
import com.example.gimapp.views.menu.MenuMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

val NULABLE_ROUTINE_EXERCISE = ExerciseRoutine(Exercise("", "", MuscularGroup.Abs), -1, -1, -1)

@HiltViewModel
class TrainingViewModel @Inject constructor(
    private val db: DataBase
) : ViewModel(), AddModeUsable, AddExerciseUsable {

    private val _routine = MutableLiveData<Routine?>()
    val routine: LiveData<Routine?> = _routine
    private val _startedRoutine = MutableLiveData<Boolean>()
    val startedRoutine: LiveData<Boolean> = _startedRoutine
    private val _training = MutableLiveData<Training>()
    val training: LiveData<Training> = _training
    private val _actualRoutineExerciseRoutineIndex = MutableLiveData<Int>()
    private val _actualExerciseRoutine =
        MutableLiveData<ExerciseRoutine?>() // null quiere decir que no quedan mas ejericicos
    private val _actualTrainingExercise = MutableLiveData<TrainingExercise>()
    val actualTrainingExercise: LiveData<TrainingExercise> = _actualTrainingExercise
    private val _remainingExerciseSets = MutableLiveData<Int>()
    val remainingExerciseSets: LiveData<Int> = _remainingExerciseSets
    private val _startTimer = MutableLiveData<Long>()
    val timer: LiveData<String> = liveData {
        while (true) {
            val timerString = calculateTimerString(_startTimer.value)
            emit(timerString) // Emitir el tiempo formateado como String
            delay(1000) // Esperar un segundo antes de calcular nuevamente
        }
    }
    private val _stateOfAddSet = MutableLiveData<AddSetState>()
    val stateOfAddSet: LiveData<AddSetState> = _stateOfAddSet
    private val _showNoRutinePopUp = MutableLiveData<Boolean>()
    val showNoRutinePopUp: LiveData<Boolean> = _showNoRutinePopUp
    private val _showChangesPopUp = MutableLiveData<Boolean>()
    val showChangesPopUp: LiveData<Boolean> = _showChangesPopUp
    private val _showSaveWithoutName = MutableLiveData<Boolean>()
    val showSaveWithoutName: LiveData<Boolean> = _showSaveWithoutName
    private val _exercises = MutableLiveData<List<Exercise>>()
    val exercises: LiveData<List<Exercise>> = _exercises
    private val _routines = MutableLiveData<List<Routine>>()
    val routines: LiveData<List<Routine>> = _routines
    private val _historical = MutableLiveData<List<TrainingExercise>>(listOf())
    val historical: LiveData<List<TrainingExercise>> = _historical
    private val _modes = MutableLiveData<List<String>>()
    val modes: LiveData<List<String>> = _modes
    private val _muscularGroup = MutableLiveData<MuscularGroup?>()
    val muscularGroup: LiveData<MuscularGroup?> = _muscularGroup
    private val _showAddMode = MutableLiveData<Boolean>()
    val showAddMode: LiveData<Boolean> = _showAddMode
    private val _showAddExercise = MutableLiveData<Boolean>()
    val showAddExercise: LiveData<Boolean> = _showAddExercise

    data class ExerciseRoutineHistorical(
        val exercise: ExerciseRoutine,
        val skipped: Boolean
    )
    private var historicalExercises: MutableList<ExerciseRoutineHistorical?> = mutableListOf()

    private var menuMessage: (@Composable (onClick: () -> Unit) -> Unit)? = null

    private lateinit var preferencesManager: PreferencesManager

    fun resetMenuMessage() {
        menuMessage = null
    }

    fun startNewTraining() {
        loadAllRoutines()
        NavigateManager.navigateTo(GimScreens.SelectRoutine)
        _routine.value = null
        _startedRoutine.value = false
    }

    fun setRoutine(r: Routine?) {
        _routine.value = r
        _actualRoutineExerciseRoutineIndex.value = 0
        _actualExerciseRoutine.value = r?.exercises?.get(0)
    }

    fun setSelected() {
        _startedRoutine.value = true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun startRoutine() {
        _training.value = Training(LocalDate.now(), mutableListOf(), _routine.value, false)
        historicalExercises.clear()
        if (_routine.value != null) NavigateManager.navigateTo(GimScreens.NextExercise)
        else navigateToAddExerciseToTraining()
    }

    fun getActualExerciseRoutine(): ExerciseRoutine {
        return _actualExerciseRoutine.value
            ?: NULABLE_ROUTINE_EXERCISE
    }

    private fun setNextActualExerciseRoutine() {
        if (_actualExerciseRoutine.value == actualExerciseRoutineByIndex()) {
            if (actualRoutineExerciseRoutineIndexIsLast()) {
                _actualExerciseRoutine.value = null
            } else {
                _actualRoutineExerciseRoutineIndex.value =
                    _actualRoutineExerciseRoutineIndex.value!! + 1
                _actualExerciseRoutine.value = actualExerciseRoutineByIndex()
            }
        } else {
            if (actualRoutineExerciseRoutineIndexIsLast()) {
                _actualExerciseRoutine.value = null
            } else {
                _actualExerciseRoutine.value = actualExerciseRoutineByIndex()
            }
        }
    }

    fun setOtherActualExerciseRoutine(
        e: ExerciseRoutine,
        mode: String,
        goNextExercise: () -> Unit,
        context: Context
    ) {
        e.exercise.mode = mode
        if (!e.isReal()) {
            showToast("Los datos no son válidos", context)
            return
        }
        if (actualRoutineExerciseRoutineIndexIsLast()) {
            _actualRoutineExerciseRoutineIndex.value = -1
        }
        _actualExerciseRoutine.value = e
        goNextExercise()
        updateTrainingDataPreferences()
    }

    fun startActualExerciseRoutine() {
        // Initialize the data
        _actualTrainingExercise.value = TrainingExercise(
            _actualExerciseRoutine.value!!.exercise, null, mutableListOf()
        )
        _remainingExerciseSets.value = _actualExerciseRoutine.value!!.sets
        updateSetState()
        NavigateManager.navigateTo(GimScreens.OnSet)
    }

    fun startTimer() {
        _startTimer.value = System.currentTimeMillis()
    }

    @SuppressLint("DefaultLocale")
    private fun calculateTimerString(startTime: Long?): String {
        if (startTime != null) {
            val currentTime = System.currentTimeMillis()
            val elapsedSeconds = (currentTime - startTime) / 1000 // Segundos transcurridos

            // Cálculo de horas y minutos
            val hours = elapsedSeconds / 60
            return if (elapsedSeconds < 6000) {
                String.format("%02d:%02d", hours, elapsedSeconds - hours * 60)
            } else {
                "99:59" // Valor límite o mensaje genérico
            }
        }
        return "00:00" // Por defecto si no hay tiempo de inicio
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getActualExcerciseRutineLastExerciseSet(): ExerciseSet? {
        return if (_actualTrainingExercise.value!!.sets.isNotEmpty())
            _actualTrainingExercise.value!!.sets.last()
        else
            _historical.value!!
                .maxByOrNull { it.date ?: LocalDate.MIN }
                ?.sets?.get(0)
    }

    fun tringToAddSet(weight: String, reps: String, context: Context) {
        val w: Double? = weight.toDoubleOrNull()
        val r: Int? = reps.toIntOrNull()
        if (w != null && r != null) {
            _actualTrainingExercise.value!!.sets.add(ExerciseSet(weight = w, reps = r, effort = -1))
            passActualSet {
                showToast(
                    "Se ha añadido correctamente\nFaltan ${_remainingExerciseSets.value!!} series",
                    context
                )
                startTimer()
            }
            updateTrainingDataPreferences()
        } else {
            showToast("Rellena los datos correctamente", context)
        }
    }

    fun skipAddSet(context: Context) {
        passActualSet {
            showToast("Se ha saltado correctamente", context)
        }
        updateTrainingDataPreferences()
    }

    private fun passActualSet(noLastCode: () -> Unit) {
        Log.d("DEV", "remainingExerciseSets: ${remainingExerciseSets.value}")
        _remainingExerciseSets.value = _remainingExerciseSets.value!! - 1
        if (_remainingExerciseSets.value == 0) {
            if (_actualTrainingExercise.value!!.sets.isNotEmpty()) {
                _training.value!!.exercises.add(_actualTrainingExercise.value!!)
                historicalExercises.add(ExerciseRoutineHistorical(_actualExerciseRoutine.value!!, false))
            } else {
                historicalExercises.add(ExerciseRoutineHistorical(_actualExerciseRoutine.value!!, true))
            }
            goNextExerciseInTraining()
            _actualTrainingExercise.value = TrainingExercise(_actualTrainingExercise.value!!.exercise, null, mutableListOf())
        } else {
            noLastCode()
        }
        updateSetState()
    }

    fun previousSet() {

    }

    private fun removeLastSet() {
        _remainingExerciseSets.value = _remainingExerciseSets.value!! + 1
        _actualTrainingExercise.value!!.sets.removeLastOrNull()
        updateSetState()
        updateTrainingDataPreferences()
    }

    fun addExtraSet() {
        _remainingExerciseSets.value = _remainingExerciseSets.value!! + 1
        updateSetState()
        updateTrainingDataPreferences()
    }

    fun navigateToAddExerciseToTraining() {
        NavigateManager.navigateTo(GimScreens.AddExerciseToTraining)
    }

    fun navigateToNextExerciseSinceAddExercise() {
        NavigateManager.navigateTo(GimScreens.NextExercise)
        historicalExercises.add(null)
    }

    fun skipExerciseInTraining() {
        historicalExercises.add(ExerciseRoutineHistorical(_actualExerciseRoutine.value!!, true))
        goNextExerciseInTraining()
        updateTrainingDataPreferences()
    }

    private fun goNextExerciseInTraining() {
        setNextActualExerciseRoutine()
        if (_actualExerciseRoutine.value != null) {
            NavigateManager.navigateCleaningNavigation(GimScreens.NextExercise)
        } else if (_training.value!!.exercises.isEmpty()) {
            setMenuMessage("El entrenamiento está vacío\nNo se ha guardado")
            NavigateManager.navigateCleaningNavigation(GimScreens.Start)
        } else {
            NavigateManager.navigateTo(GimScreens.EndRoutine)
        }
    }

    fun tryToEndActualTraining() {
        if (_routine.value == null) {
            _showSaveWithoutName.value = true
            _showNoRutinePopUp.value = true
        } else {
            if (actualTrainingEqualToRoutine()) {
                saveTrain()
            } else {
                _showChangesPopUp.value = true
            }
        }
    }

    fun saveTrainWithoutRoutine() {
        _training.value!!.routine = null
        closeChangedRoutineDialog()
        closeNoRoutineDialog()
        saveTrain()
    }

    fun saveRoutineModification() {
        _training.value!!.modifiedRoutine = true
        closeChangedRoutineDialog()
        closeNoRoutineDialog()
        saveTrain()
    }

    private fun saveTrain() {
        viewModelScope.launch {
            db.saveTraining(_training.value!!)
        }
        clearTrainingDataPreferences()
        setMenuMessage("El entrenamiento se ha guardado correctamente")
        NavigateManager.navigateCleaningNavigation(GimScreens.Start)
    }

    fun closeChangedRoutineDialog() {
        _showChangesPopUp.value = false
    }

    fun closeNoRoutineDialog() {
        _showNoRutinePopUp.value = false
    }

    fun tryToCreateNewRoutine() {
        closeChangedRoutineDialog()
        _showSaveWithoutName.value = false
        _showNoRutinePopUp.value = true
    }

    fun createNewRoutine(routineName: String) {
        closeNoRoutineDialog()

        val routineExercises = _training.value!!.exercises.map { trainingExercise ->
            val minReps = trainingExercise.sets.minOfOrNull { it.reps } ?: 0
            val maxReps = trainingExercise.sets.maxOfOrNull { it.reps } ?: 0
            val sets = trainingExercise.sets.size

            ExerciseRoutine(
                exercise = trainingExercise.exercise,
                sets = sets,
                minReps = minReps,
                maxReps = maxReps
            )
        }.toMutableList()

        val newRoutine = Routine(routineName.lowercase().trim(), routineExercises)
        viewModelScope.launch {
            db.saveRoutine(newRoutine)
            _training.value!!.routine = newRoutine
            saveTrain()
        }
    }

    fun loadAllRoutines() {
        viewModelScope.launch {
            _routines.value = db.getAllRoutines()
        }
    }

    fun updateExerciseHistorical() {
        if (_actualExerciseRoutine.value != null) {
            viewModelScope.launch() {
                _historical.value =
                    db.getExerciseTrainings(_actualExerciseRoutine.value!!.exercise)
            }
        }
    }

    fun updateMuscleExercises(m: MuscularGroup) {
        _muscularGroup.value = m
        viewModelScope.launch {
            _exercises.value = db.getExercisesByMuscle(m)
        }
    }

    private fun showToast(message: String, context: Context, length: Int = Toast.LENGTH_SHORT) {
        ToastManager.showToast(message, context, length)
    }

    fun getMenuMessage(): (@Composable (onClick: () -> Unit) -> Unit)? {
        val t = menuMessage
        return t
    }

    fun updateModesList(e: Exercise) {
        viewModelScope.launch {
            _modes.value = db.getExerciseModes(e)
        }
    }

    // Functions to improve readability
    private fun actualExerciseRoutineByIndex(): ExerciseRoutine? {
        if (_actualRoutineExerciseRoutineIndex.value!! == -1) return null
        return _routine.value?.exercises?.get(_actualRoutineExerciseRoutineIndex.value!!)
    }

    private fun actualRoutineExerciseRoutineIndexIsLast(): Boolean {
        /*
        Si el indice es -1, significa que ya se han añadido todos los ejercicios de la rutina
        y se estan añadiendo ejercios extra al final
         */
        return _actualRoutineExerciseRoutineIndex.value == -1
                || (((_actualRoutineExerciseRoutineIndex.value ?: -5) + 1)
                ) == (_routine.value?.exercises?.size ?: 0)
    }

    private fun updateSetState() {
        if (_remainingExerciseSets.value == 1 && _actualTrainingExercise.value!!.sets.size == 0)
            _stateOfAddSet.value = AddSetState.Unique
        else if (_actualTrainingExercise.value!!.sets.size == 0)
            _stateOfAddSet.value = AddSetState.First
        else if (_remainingExerciseSets.value == 1)
            _stateOfAddSet.value = AddSetState.Last
        else
            _stateOfAddSet.value = AddSetState.Normal
    }

    private fun actualTrainingEqualToRoutine(): Boolean {
        for (exerciseRutine in _routine.value!!.exercises) {
            // Buscamos el ejercicio en el entrenamiento que coincida con el de la rutina
            val trainingExercise =
                _training.value!!.exercises.find { it.exercise.name == exerciseRutine.exercise.name }
            // Si el ejercicio no está en el entrenamiento o el número de series no coincide, devolvemos falso
            if (trainingExercise == null || trainingExercise.sets.size != exerciseRutine.sets) {
                return false
            }
        }
        return true
    }

    private fun setMenuMessage(message: String) {
        menuMessage = { onClick ->
            MenuMessage(
                message = message,
                exit = { onClick() }
            )
        }
    }

    private var exerciseToAddMode: Exercise? = null

    fun openAddMode(e: Exercise) {
        _showAddMode.value = true
        exerciseToAddMode = e
    }

    override fun closeAddMode() {
        _showAddMode.value = false
    }

    override fun addModeToExercise(name: String, context: Context) {
        viewModelScope.launch {
            addModeToExercise(
                exercise = exerciseToAddMode!!,
                mode = name,
                db = db,
                trueCode = {
                    showToast("Se ha añadido el modo correctamente", context)
                    _modes.value = _modes.value!! + name
                    closeAddMode()
                },
                falseCode = {
                    showToast("Ese modo ya existe", context)
                }
            )
        }
    }

    fun openAddExercise() {
        _showAddExercise.value = true
    }

    override fun closeAddExercise() {
        _showAddExercise.value = false
    }

    override fun addExercise(exercise: Exercise, context: Context) {
        viewModelScope.launch {
            addExerciseLogic(
                exercise = exercise,
                db = db,
                trueCode = {
                    showToast("Se ha añadido correctamente", context)
                    _exercises.value = _exercises.value!! + exercise
                    closeAddExercise()
                },
                falseCode = {
                    showToast("El ejercicio que estas intentando añadir ya existe", context)
                }
            )
        }
    }

    fun goBackInNextExerciseView(context: Context) {
        /*
        Casos posibles
            - Venir desde el selector de ejercicio
                -> Volver al selector de ejercicio
            - Venir desde el selector de rutina
                -> Ir al selector de rutina
            - Venir desde otro ejercicio saltado
                -> Ir al ejercicio anterior
            - Venir como siguiente ejericico de una rutina
         */
        if (historicalExercises.isEmpty() ) {
            NavigateManager.popLastScreen()
            clearTrainingDataPreferences()
        } else {
            val last = historicalExercises.removeLastOrNull()
            if (last == null) {
                NavigateManager.navigateTo(GimScreens.AddExerciseToTraining)
            } else {
                goBackCheckingIfIsSkippedOrEndingExercise(last, context)
            }
        }
    }

    fun goBackInAddExerciseToTraining() {
        /*
        Casos posibles:
            - Venir de empezar una rutina
                -> Vovler al selector de rutina
            - Venir de hacer otro ejercicio de la rutina
                -> Volver para atras
            - Venir de añadir un ejercicio al final de la rutina
                -> Volver para atras
        */
        if (historicalExercises.isEmpty()) {
            NavigateManager.navigateCleaningNavigation(GimScreens.SelectRoutine)
        } else {
            NavigateManager.popLastScreen()
        }
    }

    fun goBackInEndRoutine(context: Context) {
        /*
        Casos posibles
            - Venir desde saltar ejercicio
                -> Volver al selector de ejercicio
            - Venir desde acabar ejercicio
                -> Ir al selector de rutina
         */
        val last = historicalExercises.removeLastOrNull()!!
        goBackCheckingIfIsSkippedOrEndingExercise(last, context)
    }

    fun goBackInAddSet(context: Context) {
        /*
        Casos posibles
            - Aun quedan series
            - Sin series
        */
        if (_actualTrainingExercise.value!!.sets.isNotEmpty()) {
            removeLastSet()
            ToastManager.showToast("Se ha eliminado la última serie", context)
        } else {
            NavigateManager.popLastScreen()
        }
    }

    private fun goBackCheckingIfIsSkippedOrEndingExercise(last: ExerciseRoutineHistorical, context: Context) {
        _actualExerciseRoutine.value = last.exercise
        if (last.skipped) {
            NavigateManager.navigateCleaningNavigation(GimScreens.NextExercise)
        } else {
            _actualTrainingExercise.value = _training.value!!.exercises.removeLastOrNull()!!
            _remainingExerciseSets.value = 0
            removeLastSet()
            NavigateManager.navigateTo(GimScreens.OnSet)
            ToastManager.showToast("Se ha eliminado la última serie", context)
        }
    }

    fun setPreferencesManager(p: PreferencesManager) {
        preferencesManager = p
    }

    private val ESPACER = "||"

    private fun clearTrainingDataPreferences() {
        viewModelScope.launch { preferencesManager.saveData("") }
    }

    private fun updateTrainingDataPreferences() {

        fun exerciseRoutineToText(e: ExerciseRoutine): String {
            return "${e.exercise.name}~~${e.exercise.mode}~~${e.sets}~~${e.minReps}~~${e.maxReps}"
        }

        /*
        ActualRoutine:
        Training:
        RoutineExerciseIndex:
        ActualExerciseRoutine
        ActualTrainingExercise
        RemainingSets
        HistoricalExercises
         */

        viewModelScope.launch {
            var text = ""

            // Actual routine (by id or 0 if is null)
            text += _routine.value?.id ?: 0

            text += ESPACER
            // Actual training (for each exercise name~~mode~~(for each set)weightxreps)
            _training.value!!.exercises.forEachIndexed { firstIndex, exercise ->
                if (firstIndex != 0) text += ",,"
                text += "${exercise.exercise.name}~~${exercise.exercise.mode}~~"
                exercise.sets.forEachIndexed { secondIndex, set ->
                    if (secondIndex != 0) text += ","
                    text += "${set.weight}x${set.reps}"
                }
            }

            text += ESPACER
            // Routine exercise index
            text += _actualRoutineExerciseRoutineIndex.value!!

            text += ESPACER
            // Actual exercise routine (name~~mode~~sets~~minReps~~maxReps)
            text += exerciseRoutineToText(_actualExerciseRoutine.value!!)

            text += ESPACER
            // Actual training exercise for each set(weightxreps)
            _actualTrainingExercise.value!!.sets.forEachIndexed { index, set ->
                if (index != 0) text += ","
                text += "${set.weight}x${set.reps}"
            }

            text += ESPACER
            // Remaining sets
            text += _remainingExerciseSets.value!!

            text += ESPACER
            // Historical of exercises screens for each (name~~mode~~sets~~minReps~~maxReps~~t or f)
            historicalExercises.forEachIndexed { index, exercise ->
                if (index != 0) text += ",,"
                text += if (exercise != null) exerciseRoutineToText(exercise.exercise) else "x"
                if (exercise != null) text += "~~" + if (exercise.skipped) "t" else "f"
            }

            // Save the data
            preferencesManager.saveData(text)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun restoreTrainingDataPreferences() {

        Log.d("DEV", "Entra")

        suspend fun parseExerciseRoutine(text: String): ExerciseRoutine {
            val parts = text.split("~~")
            if (parts.size != 5) throw Error("Incorrect exercise routine '$text' does not correct")
            val name = parts[0]
            val mode = parts[1]
            val sets = parts[2].toIntOrNull() ?: 0
            val minReps = parts[3].toIntOrNull() ?: 0
            val maxReps = parts[4].toIntOrNull() ?: 0
            return ExerciseRoutine(
                db.getExercise(name, mode) ?: throw Error("Exercise '${name}' '${mode}' does not exist"),
                sets,
                minReps,
                maxReps
            )
        }


        viewModelScope.launch {

            val savedText: String = preferencesManager.trainingData.first() ?: throw Error("Not training save")

            Log.d("DEV", "Llega con texto '$savedText'")

            val parts = savedText.split(ESPACER)
            if (parts.size < 7) throw Error("Invalid values")

            var index = 0

            // Actual routine
            val routineId = parts[index++].toLongOrNull() ?: 0L
            _routine.value = if (routineId != 0L) db.getRoutineById(routineId) else null

            // Actual training
            val trainingExercises = mutableListOf<TrainingExercise>()
            if (parts[index].isNotEmpty()) {
                parts[index].split(",,").forEach { exerciseText ->
                    val exerciseParts = exerciseText.split("~~")
                    val name = exerciseParts[0]
                    val mode = exerciseParts[1]
                    trainingExercises.add(
                        TrainingExercise(
                            db.getExercise(name, mode) ?: throw Error("Exercise '${name}' '${mode}' does not exist"),
                            null,
                            exerciseParts[2].split(",").map {
                                val setParts = it.split("x")
                                ExerciseSet(
                                    setParts[0].toDoubleOrNull() ?: throw Error("Incorrect weight in '${it}'"),
                                    setParts[1].toIntOrNull() ?: throw Error("Incorrect reps in '${it}'"),
                                    -1)
                            }.toMutableList()
                        )
                    )
                }
            }
            _training.value = Training(LocalDate.now(), trainingExercises, _routine.value, false)

            index++

            // Routine exercise index
            _actualRoutineExerciseRoutineIndex.value = parts[index++].toIntOrNull() ?: throw Error("Invalid index")

            // Actual exercise routine
            _actualExerciseRoutine.value = parseExerciseRoutine(parts[index++])

            // Actual training exercise
            _actualTrainingExercise.value = TrainingExercise(
                _actualExerciseRoutine.value!!.exercise,
                null,
                parts[index++]
                    .takeIf { it.isNotEmpty() }
                    ?.split(",")
                    ?.map {
                        val setParts = it.split("x")
                        ExerciseSet(
                            setParts[0].toDoubleOrNull() ?: throw Error("Incorrect weight in '${it}'"),
                            setParts[1].toIntOrNull() ?: throw Error("Incorrect reps in '${it}'"),
                            -1)
                    }
                    ?.toMutableList()
                    ?: mutableListOf()
            )

            // Remaining sets
            _remainingExerciseSets.value = parts[index++].toIntOrNull() ?: throw Error("Invalid remaining sets")

            // Historical of exercises screens
            historicalExercises = parts[index++]
                .takeIf { it.isNotEmpty() }
                ?.split(",,")
                ?.map {
                    if (it == "x") null
                    else {
                        Log.d("DEV", "ANTES: '$it'")
                        val parts = it.split("~~")
                        ExerciseRoutineHistorical(
                            parseExerciseRoutine(parts.dropLast(1).joinToString("~~")),
                            parts.last() == "t"
                        )
                    }
                }
                ?.toMutableList()
                ?: mutableListOf()

            // Navigate to the correct window
            updateExerciseHistorical()
            NavigateManager.navigateTo(GimScreens.SelectRoutine)
            NavigateManager.navigateTo(GimScreens.NextExercise)
            if (_actualTrainingExercise.value!!.sets.isNotEmpty()) {
                Log.d("DEV", "Entra en onset")
                NavigateManager.navigateTo(GimScreens.OnSet)
            }
        }
    }

}