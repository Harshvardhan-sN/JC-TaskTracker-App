package ind.lke.tasktracker.Room

import android.content.Context
import androidx.room.Room

object Graph {
    lateinit var database: TaskDatabase

    val taskRepository by lazy {
        TaskRepository(taskDao = database.taskDao())
    }

    fun provider(context: Context) {
        database = Room.databaseBuilder(context, TaskDatabase::class.java, "task.db").build()
    }
}