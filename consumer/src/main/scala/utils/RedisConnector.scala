package utils
import redis.clients.jedis.Jedis
import config.Config.{CACHE_URL, CACHE_VALIDITY}
import models.TaskModel.Task
import models.TaskResultModel.TaskResult
import spray.json.{JsObject, JsValue, JsonParser}
import utils.Logger.logger

import scala.concurrent.{Await, ExecutionContext, Future}
import ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success, Try}
object RedisConnector {
  private val redis = new Jedis(CACHE_URL, 6379)
  // cache validity is in seconds
  redis.configSet("timeout", CACHE_VALIDITY)
  def setTaskCache(task: Task, result: JsValue): Unit = {
    val taskResult: TaskResult = TaskResult(task.task_type, result)
    logger.info(taskResult.toJson.toString())
    logger.info(taskResult.result.toString())
    Future {
      redis.set(task.task_id, taskResult.toJson.toString())
    }
  }
  def getResult(task_id: String): Option[TaskResult] = {
    val resultFuture: Future[String] = Future {
      redis.get(task_id)
    }
    val queryOutcome: Try[String] = Try {
      Await.result(
        resultFuture,
        Duration.Inf
      )
    }
    queryOutcome match {
      case Success(result: String) =>
        JsonParser(result).convertTo[TaskResult] match {
          case res: TaskResult =>  Some(res)
          case _               => None
        }
      case Failure(e) =>
        logger.info(e.toString)
        None
    }
  }

}
