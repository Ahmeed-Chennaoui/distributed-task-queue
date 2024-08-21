package models
import spray.json.{DefaultJsonProtocol, RootJsonFormat}
import scala.jdk.CollectionConverters._
import com.datastax.oss.driver.api.core.cql.Row
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
  def extractTaskFromRow(row : Row) : Task = {
    Task(
      row.getString("task_id"),
      row.getString("task_name"),
      row.getString("task_type"),
      row.getString("parameters"),
      row.getInt("timeout"),
      row.getString("state"),
      row.getInt("attempts"),
      row.getString("period"),
      row.getInt("priority"),
      row.getList("dependencies", classOf[String]).asScala.toList
    )
  }
}
