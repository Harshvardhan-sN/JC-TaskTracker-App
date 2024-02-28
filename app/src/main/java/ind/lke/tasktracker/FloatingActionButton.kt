package ind.lke.tasktracker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.FloatingActionButton

@Composable
fun FloatingActionButton(
    viewModel: TaskViewModel
) {
    var dialogVisible by remember {
        mutableStateOf(false)
    }
    if(dialogVisible) {
        Box(contentAlignment = Alignment.Center) {
            AlertDialog(
                onDismissRequest = { dialogVisible = false },
                confirmButton = {
                    Button(onClick = {
                        dialogVisible = false
                        viewModel.deleteAllCompletedTask()
                    }) {
                        Text(text = "Yes")
                    }},
                title = { Text(text = "Are you sure") },
                text = { Text(text = "All Completed Task Entries will be Deleted") },
                dismissButton = {
                    Button(onClick = { dialogVisible = false }) {
                        Text(text = "Cancel")
                    } }
            )
        }
    }
    FloatingActionButton(modifier = Modifier.padding(20.dp),
        contentColor = Color.White,
        backgroundColor = Color.Black,
        onClick = {
            dialogVisible = true
        }) {
        Icon(imageVector = Icons.Default.Delete, contentDescription = null)
    }
}