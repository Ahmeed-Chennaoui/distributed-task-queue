import java.util.{Collections, Properties}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.kafka.clients.admin.{Admin, NewTopic}
import org.apache.kafka.common.KafkaFuture
import akka.http.scaladsl.server.Directives._
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import ch.megard.akka.http.cors.scaladsl.settings.CorsSettings
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import utils.CassandraConnector._
import routes.TaskRoutes.{addTask, getAllTasks}
import routes.MetricRoutes.getMetrics
import utils.Logger.logger
object API extends App{
  implicit val system: ActorSystem = ActorSystem("mySystem")

  if(!flag_checker(db_init_flag)){
    init_DB()
    flag_setter(db_init_flag)
  }
  private val corsSettings = CorsSettings.defaultSettings.withExposedHeaders(List("Access-Token"))

  val route =cors(corsSettings){ addTask ~ getAllTasks ~ getMetrics}
  Http().newServerAt("0.0.0.0", 8080).bind(route)
  logger.info("Server is up and running.")
}
