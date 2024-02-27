package ind.lke.tasktracker.Room

import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {
    suspend fun addTask(task: Task) = taskDao.addTask(task)
    suspend fun updateTask(task: Task) = taskDao.updateTask(task)
    fun getAllTask(): Flow<List<Task>> = taskDao.getAllTask()
    fun getTaskByID(id: Long): Flow<Task> = taskDao.getTaskByID(id)
    fun deleteAllCompletedTask() = taskDao.deleteAllCompletedTask()
}