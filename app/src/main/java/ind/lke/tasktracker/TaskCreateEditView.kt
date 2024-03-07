package ind.lke.tasktracker

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import dev.muazkadan.switchycompose.ColoredSwitch
import ind.lke.tasktracker.Room.Task
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskCreateEditView(
    id: Long,
    viewModel: TaskViewModel,
    navController: NavController
) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current
    var messageState = remember {
        mutableStateOf("Invalid Entry")
    }

    if(id != 0L) { // Update Task
        val task = viewModel.getTaskByID(id).collectAsState(initial = Task(0L, "", "", false))
        viewModel.taskNameState = task.value.name
        viewModel.taskStatusState = task.value.isCompleted
        viewModel.taskDueDateState = task.value.dueDate
    } else {
        viewModel.taskNameState = ""
        viewModel.taskStatusState = false
        viewModel.taskDueDateState = ""
    }

    val dateState = rememberSheetState()
    CalendarDialog(
        state = dateState,
        selection = CalendarSelection.Date { date ->
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            viewModel.onTaskDueDateChanged(formatter.format(date))
        },
        config = CalendarConfig(
            monthSelection = true,
            yearSelection = true,
            maxYear = 3000
            )
        )

    Scaffold(
        topBar = {
            TopAppBar(title = if (id != 0L) "Update Task" else "Create Task" ,
                navController = navController,
                onBackNavClicked = { navController.navigateUp() })
        },
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = viewModel.taskNameState,
                onValueChange = {
                    viewModel.onTaskNameChanged(it)
                },
                label = { Text(text = "Task Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            )
            Spacer(modifier = Modifier.height(5.dp))
            Row(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Select Due Date: ${viewModel.taskDueDateState}",
                    modifier = Modifier.wrapContentSize())
                IconButton(onClick = { dateState.show() }) {
                    Icon(painter = painterResource(id = R.drawable.baseline_calendar_month_24), contentDescription = "calender")
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Status: ")
                ColoredSwitch(
                    Modifier.padding(10.dp),
                    buttonHeight = 20.dp,
                    borderColor = Color.Black,
                    switchValue = viewModel.taskStatusState,
                    onValueChanged = { viewModel.onTaskStatusChanged(it) })
            }
            Spacer(modifier = Modifier.height(10.dp))
            Button(onClick = {
                if(viewModel.taskNameState.trim().isNotEmpty() &&
                    viewModel.taskDueDateState.isNotEmpty()) {
                    messageState.value = if(id == 0L) { // create task
                        viewModel.addTask(Task(
                            name = viewModel.taskNameState.trim(),
                            dueDate = viewModel.taskDueDateState,
                            isCompleted = viewModel.taskStatusState))
                        "Task Created"
                    } else {
                        viewModel.updateTask(
                            Task(
                                id = id,
                                name = viewModel.taskNameState.trim(),
                                dueDate = viewModel.taskDueDateState,
                                isCompleted = viewModel.taskStatusState)
                        )
                        "Task Updated"
                    }
                    scope.launch {
                        Toast.makeText(context, messageState.value, Toast.LENGTH_SHORT).show()
                        navController.navigateUp()
                    }
                } else {
                    // invalid entry
                    scope.launch {
                        messageState.value = "Invalid Entry"
                        scaffoldState.snackbarHostState.showSnackbar(messageState.value, duration = SnackbarDuration.Short)
                    }
                }
            }) {
                Text(text = if(id != 0L) "Update Task" else "Create Task",
                    style = TextStyle(fontSize = 18.sp)
                )
            }
        }
    }
}