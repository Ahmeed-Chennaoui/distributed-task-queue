package models
import spray.json._
import spray.json.JsonParser.ParsingException
import utils.Logger.logger
object TaskParamsModel extends DefaultJsonProtocol {
  trait TaskParam
  case class Task1Params(param1: String) extends TaskParam
  implicit val task1ParamsFormat: RootJsonFormat[Task1Params] = jsonFormat1(
    Task1Params
  )
  case class NoParams() extends TaskParam
  implicit val noParamsFormat: RootJsonFormat[NoParams] = jsonFormat0(NoParams)
  case class nextPrimeParams(n: String) extends TaskParam
  implicit val nextPrimeParamsFormat: RootJsonFormat[nextPrimeParams] =
    jsonFormat1(nextPrimeParams)
  case class benchmarkPrimeNumbersParams(p : String , q : String ) extends TaskParam
  implicit val benchmarkPrimeNumbersParamsFormat : RootJsonFormat[benchmarkPrimeNumbersParams] = jsonFormat2(benchmarkPrimeNumbersParams)

  def parseTaskParams[T <: TaskParam](
      jsonString: String
  )(implicit jsonReader: JsonReader[T]): Option[TaskParam] = {
    try {
      JsonParser(jsonString).convertTo[T] match {
        case taskParam: T => Some(taskParam)
        case _            => None
      }
    } catch {
      case e: ParsingException => {
        logger.error("Invalid Task JSON Format")
        None
      }
      case _: Throwable => None
    }
  }
}
