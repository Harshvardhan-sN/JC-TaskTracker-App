package ind.lke.tasktracker.Room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task-table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "task-name")
    val name: String = "",
    @ColumnInfo(name = "task-dueDate")
    val dueDate: String = "",
    @ColumnInfo(name = "task-status")
    val isCompleted: Boolean = false
)