package ind.lke.tasktracker

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ind.lke.tasktracker.Room.Graph
import ind.lke.tasktracker.Room.Task
import ind.lke.tasktracker.Room.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TaskViewModel(
    private val taskRepository: TaskRepository = Graph.taskRepository
): ViewModel() {
    var taskNameState by mutableStateOf("")
    var taskDueDateState by mutableStateOf("")
    var taskStatusState by mutableStateOf(false)

    fun onTaskNameChanged(newString: String) {
        taskNameState = newString
    }
    fun onTaskDueDateChanged(newString: String) {
        taskDueDateState = newString
    }
    fun onTaskStatusChanged(newBool: Boolean) {
        taskStatusState = newBool
    }

    lateinit var getAllTask: Flow<List<Task>>
    init {
        viewModelScope.launch {
            getAllTask = taskRepository.getAllTask()
        }
    }

    fun addTask(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        taskRepository.addTask(task)
    }
    fun updateTask(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        taskRepository.updateTask(task)
    }
    fun deleteAllCompletedTask() = viewModelScope.launch(Dispatchers.IO) {
        taskRepository.deleteAllCompletedTask()
    }
    fun getTaskByID(id: Long): Flow<Task> = taskRepository.getTaskByID(id)
}