package com.programmazionemobile.myapplication.ui.addedittask

import android.renderscript.RenderScript.Priority
import com.programmazionemobile.myapplication.data.TaskDao
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.programmazionemobile.myapplication.ui.ADD_TASK_RESULT_OK
import com.programmazionemobile.myapplication.ui.EDIT_TASK_RESULT_OK
import com.programmazionemobile.myapplication.data.Task
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

//aggiorna il DB tramite il TaskDao
//VM per FRAGMENT
class AddEditTaskViewModel @ViewModelInject constructor(
    private val taskDao: TaskDao,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    val task = state.get<Task>("task")


    var taskName = state.get<String>("taskName") ?: task?.name ?: ""
        set(value) {
            field = value
            state.set("taskName", value)
        }

    var taskImportance = state.get<Boolean>("taskImportance") ?: task?.important ?: false
        set(value) {
            field = value
            state.set("taskImportance", value)
        }

    var taskPriority = state.get<Int>("taskPriority") ?: task?.priority ?: 0
        set(value) {
            field = value
            state.set("taskPriority", value)
        }

    var taskDuration = state.get<Int>("taskDuration") ?: task?.duration ?: 0
        set(value) {
            field = value
            state.set("taskDuration", value)
        }

    //Channel per communicare gli eventi all'intf utente
    private val addEditTaskEventChannel = Channel<AddEditTaskEvent>()
    val addEditTaskEvent = addEditTaskEventChannel.receiveAsFlow()

    fun onSaveClick() {
        if (taskName.isBlank()) {
            showInvalidInputMessage("Il nome non può essere vuoto")
            return
        }

        if (task != null) {
            val updatedTask = task.copy(name = taskName, important = taskImportance, priority = taskPriority, duration = taskDuration)
            updateTask(updatedTask)
        } else {
            val newTask = Task(name = taskName, important = taskImportance, priority = taskPriority, duration = taskDuration)
            createTask(newTask)
        }
    }

    private fun createTask(task: Task) = viewModelScope.launch {
        if (taskDao.getTaskByName(task.name) != null) {
            showInvalidInputMessage("Questo nome esiste già in un task. ")
            return@launch
        }
        taskDao.insert(task)
        addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackWithResult(ADD_TASK_RESULT_OK))
    }

    private fun updateTask(task: Task) = viewModelScope.launch {
        taskDao.update(task)
        addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackWithResult(EDIT_TASK_RESULT_OK))
    }

    private fun showInvalidInputMessage(text: String) = viewModelScope.launch {
        addEditTaskEventChannel.send(AddEditTaskEvent.ShowInvalidInputMessage(text))
    }

    private fun priorityToString(priority: Int): String {
        return when (priority) {
            1 -> "Alta"
            2 -> "Media"
            3 -> "Bassa"
            else -> "" // Tratta il caso di default o errore
        }
    }


    fun updatePriority(priority: Int) {
        // Aggiorna la priorità nel view model
        taskPriority = priority
        // Imposta correttamente il valore selezionato nel Spinner
        state.set("taskPriority", priorityToString(priority))

        // Aggiorna il valore direttamente nella mappa SavedStateHandle
        state["taskPriority"] = priorityToString(priority)
    }

    sealed class AddEditTaskEvent {
        data class ShowInvalidInputMessage(val msg: String) : AddEditTaskEvent()
        data class NavigateBackWithResult(val result: Int) : AddEditTaskEvent()
    }
}