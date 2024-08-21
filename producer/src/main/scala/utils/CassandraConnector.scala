package utils
import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.core.cql.ResultSet
import config.Config.{DB_PORT, DB_URL}
import java.net.InetSocketAddress
import utils.Logger.logger
object CassandraConnector {
  private val InetSocket = new InetSocketAddress(DB_URL, DB_PORT)
  val session: CqlSession =
    CqlSession
      .builder()
      .addContactPoint(InetSocket)
      .withLocalDatacenter("datacenter1")
      .build()

  def query_exec_result(query: String) /*(implicit session : CqlSession = CassandraConnector.session)*/: Option[ResultSet] = {
    try {
      val res = session.execute(query)
      Some(res)

    } catch {
      case e: Throwable =>
        logger.error(
          "An error occurred while executing a database query : " + e.toString
        )
        None

    }
  }
  def query_exec(query:String): Unit = {
    session.execute(query)

  }
}
