package utils
import com.datastax.oss.driver.api.core.CqlSession
import config.Config.{DB_PORT, DB_URL}
import java.net.InetSocketAddress
object CassandraConnector {
  private val InetSocket = new InetSocketAddress(DB_URL, DB_PORT)
  private val session: CqlSession =
    CqlSession
      .builder()
      .addContactPoint(InetSocket)
      .withLocalDatacenter("datacenter1")
      .build()

  def query_exec(query:String): Unit = {
    session.execute(query)
  }
}
