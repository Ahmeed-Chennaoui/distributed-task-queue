import scala.collection.JavaConverters._
import config.Config._
import controllers.TaskControllers.attemptTask
import models.TaskModel._
import utils.Logger.logger
import utils.KafkaConnector.consumer
import controllers.TaskRunners.nextPrime
object Consumer extends App {
  // Subscribe to the topics
  private val topicList : List[String] = topic.split(",").toList
  logger.info(topicList.toString())
  consumer.subscribe(topicList.asJava)
  logger.info(s"Subscribed to topic $topic")

  // Poll for messages
  while (true) {
    val records = consumer.poll(100)
    records.forEach { record =>
      logger.info(s"Found a task with ID : ${record.key()}")
      parseTask(record.value()) match {
        case Some(task) => attemptTask(task)
        case None => logger.error("An error occurred when parsing the task.")
      }
    }
  }
}
