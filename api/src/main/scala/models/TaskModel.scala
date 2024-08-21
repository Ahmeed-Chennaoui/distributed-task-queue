package models

import com.datastax.oss.driver.api.core.cql.Row
import java.time.Instant
import spray.json.{
  DefaultJsonProtocol,
  DeserializationException,
  JsArray,
  JsNull,
  JsObject,
  JsString,
  JsValue,
  JsonFormat,
  RootJsonFormat,
  SerializationException
}

import scala.jdk.CollectionConverters.CollectionHasAsScala

object TaskModel extends DefaultJsonProtocol {
  case class Task(
      task_name: String,
      task_type: String,
      parameters: JsObject,
      timeout: Int,
      priority: Option[Int],
      dependencies: Option[List[String]]
  )
  implicit object OptionalStringList extends JsonFormat[Option[List[String]]] {
    override def read(json: JsValue): Option[List[String]] = json match {
      case JsNull => None
      case JsArray(elements) =>
        Some(elements.collect {
          case JsString(value) => value;
          case _ =>
            throw new DeserializationException("Expected JsString or JsNull")
        }.toList)
    }

    override def write(obj: Option[List[String]]): JsValue = obj match {
      case None => JsNull
      case Some(list: List[String]) =>
        JsArray(list.map(el => JsString(el)).toVector)
      case _ =>
        throw new SerializationException("Expected None or Some(List[String])")
    }
  }
  implicit val TaskFormat: RootJsonFormat[Task] = jsonFormat6(Task)
  case class TaskInfo(
      task_id: String,
      task_name: String,
      task_type: String,
      parameters: String,
      timeout: Int,
      state: String,
      attempts: Int,
      period: String,
      priority: Int,
      dependencies: List[String],
      created_at: Long,
      result : String
  )
  implicit val TaskInfoFormat: RootJsonFormat[TaskInfo] = jsonFormat12(TaskInfo)
  def extractTaskFromRow(row: Row): TaskInfo = {
    TaskInfo(
      row.getString("task_id"),
      row.getString("task_name"),
      row.getString("task_type"),
      row.getString("parameters"),
      row.getInt("timeout"),
      row.getString("state"),
      row.getInt("attempts"),
      row.getString("period"),
      row.getInt("priority"),
      row.getList("dependencies", classOf[String]).asScala.toList,
      row.getInstant("created_at").getEpochSecond,
      row.getString("result")
    )
  }
}
