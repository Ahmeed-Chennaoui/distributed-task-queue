package controllers

import com.datastax.oss.driver.api.core.cql.ResultSet
import models.TaskModel._
import utils.CassandraConnector.{query_exec, query_exec_result}
import config.Config.{PeriodType, keyspace}
import config.Config.States
import utils.ResultSetManipulator.resultSetToList
import java.util.UUID
import java.util.Calendar
import scala.annotation.tailrec
import scala.util.Try
object TaskControllers {

  def getAllTasksFromDatabase: List[TaskInfo] = {
    val query: String = s"SELECT * FROM ${keyspace}.tasks"
    val result = query_exec_result(query)
    result match {
      case None => List()
      case Some(resultSet: ResultSet) => {
        resultSetToList[TaskInfo](resultSet, extractTaskFromRow)
      }
    }
  }
  private def getTaskPeriod(timestamp: Long, periodType: String): String = {
    val cal = Calendar.getInstance()
    cal.setTimeInMillis(timestamp)
    val day = cal.get(Calendar.DAY_OF_MONTH)
    val week = cal.get(Calendar.WEEK_OF_YEAR)
    val month = cal.get(Calendar.MONTH)
    val year = cal.get(Calendar.YEAR)
    val quarter = month / 3 + 1

    periodType match {
      case "Day"     => s"$day-${month + 1}-$year"
      case "Week"    => s"$week-$year"
      case "Month"   => s"${month + 1}-$year"
      case "Quarter" => s"$quarter-$year"
      case "Year"    => s"$year"
      case _         => throw new Exception("Invalid Period Type.")
    }
  }
  def prepareQuery(t: Task): String = {
    val timestamp = System.currentTimeMillis()
    val period = getTaskPeriod(timestamp, PeriodType)
    val priority: Int = t.priority match {
      case Some(priority) => priority
      case None           => 0
    }
    val dependencies: String = t.dependencies match {
      case None => "[]"
      case Some(list: List[String]) =>
        list.map(item => "'" + item + "'").mkString("[", ",", "]")
    }
    s"""
    |INSERT INTO $keyspace.tasks
    |(task_id, task_name, task_type, parameters, attempts, timeout, state, created_at, period, priority, dependencies, result)
    |VALUES
    |('${UUID
      .randomUUID()}', '${t.task_name}', '${t.task_type}', '${t.parameters}',
    | ${0}, ${t.timeout}, '${States.Pending}', '$timestamp', '$period',$priority , $dependencies,'');
     """.stripMargin
  }
  def addTaskToDatabase(t: Task): Try[Unit] = {
    val query = prepareQuery(t)
    Try(query_exec(query))
  }
}
