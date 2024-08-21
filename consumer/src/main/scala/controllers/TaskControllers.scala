package controllers

import config.Config.{States, keyspace}
import controllers.TaskRunners.runTask
import models.TaskModel.Task
import spray.json.JsValue
import utils.CassandraConnector.query_exec
import utils.Logger.logger

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future, TimeoutException}
import scala.util.{Failure, Success, Try}
import scala.concurrent.duration._
import utils.RedisConnector.setTaskCache

object TaskControllers {

  private def updateTaskState(task: Task, state: States.State): Future[Unit] = {
    val query =
      s"UPDATE $keyspace.tasks SET state = '$state' WHERE period='${task.period}' AND priority=${task.priority} AND task_id = '${task.task_id}'"
    Future {
      query_exec(query)
    }
  }
  private def incrementTaskAttempts(task: Task): Future[Unit] = {
    val query =
      s"UPDATE $keyspace.tasks SET attempts = ${task.attempts + 1} WHERE period='${task.period}' AND priority=${task.priority} AND task_id = '${task.task_id}'"
    Future {

      query_exec(query)
    }
  }

  private def updateResult(task: Task, result: String): Future[Unit] = {
    val query =
      s"UPDATE $keyspace.tasks SET result = '$result' WHERE period='${task.period}' AND priority=${task.priority} AND task_id = '${task.task_id}'"
    Future {

      query_exec(query)
    }
  }

  def attemptTask(task: Task): Unit = {
    val taskFuture: Future[JsValue] = Future { runTask(task) }
    Await.ready(incrementTaskAttempts(task), Duration.Inf)
    Await.ready(
      Future { updateTaskState(task, States.Processing) },
      Duration.Inf
    )
    val taskOutcome: Try[JsValue] = Try {
      Await.result(taskFuture, task.timeout.seconds)
    }
    taskOutcome match {
      case Success(_) =>
        Await.ready(
          Future {
            updateTaskState(task, States.Completed)
            setTaskCache(task, taskOutcome.get)
            updateResult(task, taskOutcome.get.toString())
          },
          Duration.Inf
        )
        logger.info("Task completed successfully.")
      case Failure(e: TimeoutException) =>
        val timeoutFuture = updateTaskState(task, States.TimedOut)
        logger.error(s"Task timed out : took longer than ${task.timeout}.")
        timeoutFuture.onComplete { case _ => System.exit(0) }
      case Failure(e: Exception) =>
        updateTaskState(task, States.Failed)
        logger.error(
          "Task execution failed : " + e.getStackTrace.mkString("\n")
        )
    }
  }
}
