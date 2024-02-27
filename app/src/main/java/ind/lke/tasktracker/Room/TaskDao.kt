package ind.lke.tasktracker.Room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
abstract class TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun addTask(taskEntity: Task)

    @Update
    abstract suspend fun updateTask(taskEntity: Task)

    @Query("DELETE FROM `task-table` WHERE `task-status` == 1")
    abstract fun deleteAllCompletedTask()

    @Query("SELECT * FROM `task-table`")
    abstract fun getAllTask(): Flow<List<Task>>

    @Query("SELECT * FROM `task-table` WHERE id = :id")
    abstract fun getTaskByID(id: Long): Flow<Task>
}