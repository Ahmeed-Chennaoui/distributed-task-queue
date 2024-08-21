import models.TaskModel._
import utils.Logger.logger
import controllers.TaskControllers._
import akka.actor.ActorSystem
import config.Config.topics
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.util.{Failure, Success}
import utils.KafkaConnector.createTopic
object Producer extends App {
  // demo code
  topics.split(",").foreach(topic => createTopic(topic,4,2))
  implicit val system: ActorSystem = ActorSystem("mySystem")
  system.scheduler.scheduleAtFixedRate(50.milliseconds, 3.seconds)(
    new Runnable {
      override def run(): Unit = {
        loadEligibleTaskListFromDB /*()*/ match {
          case None => logger.info("No Tasks Available For Processing !")
          case Some(taskList: List[Task]) =>
            taskList
              .foreach(task => {
                logger.info(s"Adding Task $task to Queue ...")
                addTaskToQueue(task) match{
                  case Success(_) => logger.info(s"$task added successfully.")
                  case Failure(err)=> logger.error(s"Failed to add $task to queue :" + err.toString)
                }
              })
        }
      }
    }
  )
}
