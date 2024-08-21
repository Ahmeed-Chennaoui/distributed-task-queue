package controllers
import controllers.TaskControllers.getAllTasksFromDatabase
import models.TaskModel.TaskInfo
import models.MetricModel._
object MetricControllers {
  def updateMetrics(): Unit = {
    val tasks: List[TaskInfo] = getAllTasksFromDatabase
    val statusCounts: Map[String, Int] =
      tasks.foldLeft(Map.empty[String, Int]) { (counts, task) =>
        counts + (task.state -> (counts.getOrElse(task.state, 0) + 1))
      }
    /* We reset the values to zero to avoid the bug where the old value is not changed
      if the number of processing tasks = 1
      then the task finishes the number of processing tasks is 0
      => this means the map no longer contains the key processing
      => the old value no longer gets updated
     */
    completedTasks.set(0)
    processingTasks.set(0)
    pendingTasks.set(0)
    failedTasks.set(0)
    queuedTasks.set(0)
    timedOutTasks.set(0)
    statusCounts.foreach { case (key: String, value: Int) =>
      key match {
        case "Completed"  => completedTasks.set(value)
        case "Processing" => processingTasks.set(value)
        case "Pending"    => pendingTasks.set(value)
        case "Failed"     => failedTasks.set(value)
        case "Queued"     => queuedTasks.set(value)
        case "TimedOut"   => timedOutTasks.set(value)
      }
    }
  }
}
