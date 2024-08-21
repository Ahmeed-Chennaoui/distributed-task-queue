package models
import spray.json._
import spray.json.JsonParser.ParsingException
import utils.Logger.logger
object TaskModel extends DefaultJsonProtocol {

  case class Task(task_id: String,
                  task_name: String,
                  task_type: String,
                  parameters: String,
                  timeout: Int,
                  state: String,
                  attempts: Int,
                  period: String,
                  priority: Int,
                  dependencies: List[String]
                 )
  implicit val TaskFormat: RootJsonFormat[Task] = jsonFormat10(Task)

  def parseTask(jsonString: String): Option[Task] = {
    try{
      JsonParser(jsonString).convertTo[Task] match {
        case task: Task => Some(task)
        case _ => None
      }
    }
    catch {
      case e: ParsingException=> {
        logger.error("Invalid Task Json Format" + e.summary)
        None
      }
      case _ => None
    }
  }

}
