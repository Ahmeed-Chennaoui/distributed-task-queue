package models
import spray.json._
import spray.json.JsonParser.ParsingException
object TaskResultModel extends  DefaultJsonProtocol {
  case class TaskResult(task_type:String, result : JsValue)
  implicit val TaskResultFormat : RootJsonFormat[TaskResult] = jsonFormat2(TaskResult)


}
