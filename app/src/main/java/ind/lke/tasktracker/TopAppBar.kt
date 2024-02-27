package ind.lke.tasktracker

import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@Composable
fun TopAppBar(
    title: String,
    navController: NavController,
    onBackNavClicked: () -> Unit = {}
) {
    val navigationIcon: (@Composable () -> Unit)? =
        if(!title.contains("TaskTracker")) {   // if we are on main screen
            {
                IconButton(onClick = { onBackNavClicked() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = Color.Black
                    )
                }
            }
        } else { null }

    TopAppBar(
        actions = {
                  if (title == "TaskTracker") {
                      IconButton(onClick = { navController.navigate(Screen.AddScreen.route + "/0L") }) {
                          Icon(imageVector = Icons.Default.Add, contentDescription = null)
                      }
                  }
        },
        title = {
            Text(text = title,
                color = Color.Black,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .heightIn(max = 24.dp))
        },
        elevation = (-1).dp,
        backgroundColor = colorResource(id = R.color.app_bar_color),
        navigationIcon = navigationIcon
    )
}