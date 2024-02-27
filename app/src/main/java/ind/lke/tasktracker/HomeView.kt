package ind.lke.tasktracker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ind.lke.tasktracker.Room.Task

@Composable
fun HomeView(
    navController: NavController,
    viewModel: TaskViewModel
) {
    val context = LocalContext.current
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(modifier = Modifier.padding(20.dp),
                contentColor = Color.White,
                backgroundColor = Color.Black,
                onClick = {
                    viewModel.deleteAllCompletedTask()
                }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
        },
        topBar = {
            TopAppBar(title = "TaskTracker", navController = navController)
        }
    ) {
        val taskList = viewModel.getAllTask.collectAsState(initial = listOf())
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(it)) {
            items(taskList.value) { task ->
                TaskItem(task = task) {
                    val id = task.id
                    navController.navigate(Screen.AddScreen.route + "/$id")
                }
            }
        }
    }
}

@Composable
fun TaskItem(task: Task, onClick: () -> Unit) {
    val textColor = if (task.isCompleted) Color.Green else Color.Red
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 8.dp, end = 8.dp)
            .clickable { onClick() },
        elevation = 10.dp
        // background
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Text(text = task.name, fontWeight = FontWeight.ExtraBold)
            Row(
                modifier = Modifier.padding(top = 5.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Due Date: ${task.dueDate}")
                Text(text = "Status: ${if(task.isCompleted) "Completed" else "Incomplete"}",
                        color = textColor)
            }
        }
    }
}