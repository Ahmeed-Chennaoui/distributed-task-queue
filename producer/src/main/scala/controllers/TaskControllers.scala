package controllers
import com.datastax.oss.driver.api.core.CqlSession
import models.TaskModel._
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import utils.KafkaConnector.producer
import utils.CassandraConnector.{query_exec, query_exec_result}
import utils.ResultSetManipulator.resultSetToList
import config.Config.{MAX_ATTEMPTS, States, keyspace}
import utils.CassandraConnector
import utils.Logger.logger
import utils.RedisConnector.checkTask

import scala.annotation.tailrec
import scala.util.Try
object TaskControllers {
  def loadEligibleTaskListFromDB/*( session : CqlSession = CassandraConnector.session)*/: Option[List[Task]] = {
    val query = s"Select * from ${keyspace}.tasks"
    query_exec_result(query) /*(session)*/ match {
      case None => {
        logger.error("An error occurred while loading tasks from database.")
        None
      }
      case Some(resultSet) =>
        val resList = resultSetToList[Task](resultSet, extractTaskFromRow)
          .filter(isEligibleTask)
        if (resList.isEmpty) None
        else Some(resList)
    }

  }
  def addTaskToQueue(task: Task,producer : KafkaProducer[String,String]=producer): Try[Unit] = {
    val value = task.toJson.toString()
    val key = task.task_id
    val topic = task.task_type
    val record = new ProducerRecord[String, String](topic, key, value)
    Try({
      producer.send(record)
      updateTaskState(task, States.Queued)
    })
  }
  private def updateTaskState(task: Task, state: States.State): Unit = {
    val query =
      s"UPDATE $keyspace.tasks SET state = '$state' WHERE period='${task.period}' AND priority=${task.priority} AND task_id = '${task.task_id}'"
    query_exec(query)
  }

  private def dependenciesVerified(dependencies: List[String]): Boolean = {
    @tailrec
    def verify(accumulator: Boolean, iterator: Iterator[String]): Boolean = {
      if (iterator.hasNext && accumulator) {
        val current = iterator.next()
        verify(checkTask(current), iterator)
      } else {
        accumulator
      }
    }
    verify(true, dependencies.iterator)
  }

  private def isEligibleTask(task: Task): Boolean = {
    import States._
    if (!dependenciesVerified(task.dependencies)) false
    else {
      task match {
        case Task(_, _, _, _, _, state, _, _, _, _)
            if (state.matches(s"${Queued}|${Completed}|${Processing}")) => false
        case Task(_, _, _, _, _, _, attempts, _, _, _)
            if attempts >= MAX_ATTEMPTS =>false
        case _ => true
      }
    }

  }
}
