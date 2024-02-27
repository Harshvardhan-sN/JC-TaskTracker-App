package ind.lke.tasktracker

import android.app.Application
import ind.lke.tasktracker.Room.Graph

class TaskTrackerApp: Application() {

    override fun onCreate() {
        super.onCreate()
        Graph.provider(this@TaskTrackerApp)
    }
}