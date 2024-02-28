package ind.lke.tasktracker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ind.lke.tasktracker.Room.Task
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeView(
    navController: NavController,
    viewModel: TaskViewModel
) {
    val context = LocalContext.current
    val taskList = viewModel.getAllTask.collectAsState(initial = listOf())
    val taskListState = remember(taskList.value) {
        mutableStateOf(taskList.value)
    }
    val selectedOptionState = remember {
        mutableStateOf("empty")
    }
    LaunchedEffect(taskList.value) {
        taskListState.value = taskList.value
    }
    fun parseStringToDate(dateString: String): Date{
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return format.parse(dateString) ?: Date()
    }
    val sortedTaskState = remember(taskListState.value, selectedOptionState.value) {
        derivedStateOf {
            when(selectedOptionState.value) {
                "dueDate" -> taskListState.value.sortedBy { parseStringToDate(it.dueDate) }
                "status" -> taskListState.value.sortedBy { it.isCompleted }
                else -> taskListState.value
            }
        }
    }

    var showPopUpMenu by remember {
        mutableStateOf(false)
    }
    if(showPopUpMenu) {
        Box(modifier = Modifier.fillMaxWidth().wrapContentSize(Alignment.TopEnd)) {
            DropdownMenu(
                expanded = showPopUpMenu,
                onDismissRequest = { showPopUpMenu = false },
                modifier = Modifier.wrapContentSize()
            ) {
                DropdownMenuItem(onClick = {
                    selectedOptionState.value = "dueDate"
                    showPopUpMenu = false
                }) {
                    Text(text = "Sort by Due Date")
                }
                DropdownMenuItem(onClick = {
                    selectedOptionState.value = "status"
                    showPopUpMenu = false
                }) {
                    Text(text = "Sort by Task Status")
                }
            }
        }
    }
    Scaffold(
        floatingActionButton = { FloatingActionButton(viewModel = viewModel) },
        topBar = { TopAppBar(title = "TaskTracker", navController = navController) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            IconButton(
                onClick = { showPopUpMenu = true },
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(painter = painterResource(id = R.drawable.baseline_sort_24), contentDescription = "sortIcon")
            }
            LazyColumn(modifier = Modifier
                .fillMaxSize()) {
                items(sortedTaskState.value) { task ->
                    TaskItem(task = task) {
                        val id = task.id
                        navController.navigate(Screen.AddScreen.route + "/$id")
                    }
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
                modifier = Modifier
                    .padding(top = 5.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Due Date: ${task.dueDate}")
                Text(
                    text = "Status: ${if (task.isCompleted) "Completed" else "Incomplete"}",
                    color = textColor
                )
            }
        }
    }
}