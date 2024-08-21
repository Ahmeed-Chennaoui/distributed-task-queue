package routes

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import controllers.TaskControllers._
import models.TaskModel._

import scala.util.{Success,Failure}

object TaskRoutes {
  val addTask: Route = (path("task") & post){
  entity(as[Task]){
    case Task(task_name, task_type, parameters, timeout,priority,dependencies) =>
        val task = Task(task_name, task_type, parameters, timeout,priority,dependencies)
        addTaskToDatabase(task) match {
          case Success(_) => complete(HttpResponse(StatusCodes.OK,entity="Task created successfully."))
          case Failure(err) => complete(HttpResponse(StatusCodes.BadRequest,entity="An error occurred :" + err.getMessage))
        }
    case _ => complete(HttpResponse(StatusCodes.BadRequest,entity="Invalid Parameters."))

  }
  }
  val getAllTasks : Route = (path("tasks") & get){
    complete(getAllTasksFromDatabase)
  }
}
