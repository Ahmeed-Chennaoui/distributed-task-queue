package models
import spray.json._
object TaskParamsModel extends DefaultJsonProtocol {
  trait TaskParam
  case class Task1Params(param1:String) extends TaskParam
  implicit val task1ParamsFormat : RootJsonFormat[Task1Params] = jsonFormat1(Task1Params)
  case class Task2Params(param2:String) extends TaskParam
  implicit val task2ParamsFormat : RootJsonFormat[Task2Params] = jsonFormat1(Task2Params)
  case class NoParams() extends TaskParam
  implicit val noParamsFormat : RootJsonFormat[NoParams] = jsonFormat0(NoParams)


}
