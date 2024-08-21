package controllers
import models.TaskModel.Task
import models.TaskParamsModel._
import models.TaskResultModel.TaskResult
import spray.json.{JsNumber, JsObject, JsString, JsValue}
import utils.RedisConnector.getResult
import utils.Logger.logger

import scala.annotation.tailrec
object TaskRunners {

  def runTask(task: Task): JsValue = {
    task.task_type match {
      case "test"                      => runJob1(task.parameters)
      case "fail"                      => failedJob()
      case "long"                      => longJob()
      case "first_prime_larger_than_n" => nextPrime(task.parameters)
      case "benchmark_primes" =>
        benchmarkPrimeNumbers(task.parameters, task.dependencies)
      case _ =>
        logger.error("No such task !")
        throw new Exception("No such Task!")
    }
  }
  def runJob1(paramsJsonString: String): JsValue = {
    parseTaskParams[Task1Params](paramsJsonString) match {
      case Some(task1Params: Task1Params) =>
        logger.info(s"Running job1 with parameters : $task1Params")
      case None => throw new Exception("Invalid Task params")

    }
    new JsObject(Map())
  }
  def failedJob() = {
    throw new Exception("Error! Just because i feel like it.")
  }
  def longJob(): JsValue = {
    Thread.sleep(20000L)
    logger.info(s"done from future ")
    new JsObject(Map())
  }
  def nextPrime(paramsJsonString: String): JsValue = {
    parseTaskParams[nextPrimeParams](paramsJsonString) match {
      case Some(params: nextPrimeParams) =>
        val n: BigInt = BigInt(params.n)
        def isPrime(n: BigInt, limit: BigInt): Boolean = {
          if (n < BigInt(2)) return false
          var cur: BigInt = 2
          // if i do this tail recursively i get stack overflow for large numbers
          while (cur < limit) {
            if (n.mod(cur).equals(BigInt(0))) {
              logger.info(s"${n} is not prime")
              return false
            }
            cur = cur + 1
          }
          true
        }
        var candidate: BigInt = n + 1
        while (!isPrime(candidate, candidate - 1)) {
          candidate = candidate + 1
        }
        JsNumber(candidate)
      case None => throw new Exception("Invalid Task params")
    }
  }
  def benchmarkPrimeNumbers(
      paramsJsonString: String,
      dependencies: List[String]
  ): JsValue = {
    def benchmarkDuration(p: BigInt, q: BigInt): JsString = {
      val start: Long = System.nanoTime()
      val product: BigInt = p * q
      var candidate: BigInt = 2
      while (!product.mod(candidate).equals(0)) {
        candidate = candidate + 1
      }
      val duration: Long = System.nanoTime() - start
      val tempSec = duration / (1000 * 1000 * 1000);
      val sec = tempSec % 60;
      val min = (tempSec / 60) % 60;
      val hour = (tempSec / (60 * 60)) % 24;
      val day = (tempSec / (24 * 60 * 60)) % 24;
      val res = String.format("%dd %dh %dm %ds", day, hour, min, sec)
      JsString(res)
    }
    val dependencyResults = dependencies
      .flatMap((dep: String) => getResult(dep))
      .filter(res => res.task_type == "first_prime_larger_than_n")
    logger.info(dependencies.toString())
    logger.info(dependencyResults.toString())

    if (dependencyResults.length == 2) {
      val pString: String = dependencyResults.head.result.toString
      val qString: String = dependencyResults(1).result.toString
      val p: BigInt = BigInt(pString)
      val q: BigInt = BigInt(qString)
      logger.info(s"p : $p , q : $q")
      benchmarkDuration(p, q)
    } else {
      parseTaskParams[benchmarkPrimeNumbersParams](paramsJsonString) match {
        case Some(params: benchmarkPrimeNumbersParams) =>
          val p: BigInt = BigInt(params.p)
          val q: BigInt = BigInt(params.q)
          logger.info(s"p : $p , q : $q")
          benchmarkDuration(p, q)

        case None => throw new Exception("Invalid Task params")
      }
    }

  }
}
