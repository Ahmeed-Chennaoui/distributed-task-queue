package models
import io.prometheus.client.{Counter, Gauge}
object MetricModel {
  val completedTasks: Gauge = Gauge
    .build()
    .name("completed_tasks")
    .help("Number of completed tasks")
    .register()
  val failedTasks: Gauge = Gauge
    .build()
    .name("failed_tasks")
    .help("Number of failed tasks")
    .register()
  val pendingTasks : Gauge = Gauge
    .build()
    .name("pending_tasks")
    .help("Number of pending tasks")
    .register()
  val queuedTasks: Gauge = Gauge
    .build()
    .name("queued_tasks")
    .help("Number of queued tasks")
    .register()
  val timedOutTasks: Gauge = Gauge
    .build()
    .name("timedOut_tasks")
    .help("Number of timedOut tasks")
    .register()
  val processingTasks: Gauge = Gauge
    .build()
    .name("processing_tasks")
    .help("Number of processing tasks")
    .register()
}
