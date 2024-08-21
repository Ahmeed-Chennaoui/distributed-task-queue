package utils
import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.core.cql.ResultSet
import config.Config.{DB_PORT, DB_URL, keyspace}
import java.net.InetSocketAddress
import scala.io.Source
import config.InitData.queries
import utils.Logger.logger
object CassandraConnector {
  val db_init_flag = "database_init_success"

  private val InetSocket = new InetSocketAddress(DB_URL, DB_PORT)
  private val session: CqlSession =
    CqlSession
      .builder()
      .addContactPoint(InetSocket)
      .withLocalDatacenter("datacenter1")
      .build()

  def Init_DB_From_File(): Unit = {
    val cqlFile = "src/main/resources/Init.cql"
    val source = Source.fromFile(cqlFile)
    val queries = source.mkString.split(";").map(_.trim).filter(_.nonEmpty)
    queries.foreach(query => {
      try {
        session.execute(query)

      } catch {
        case e: Throwable => println(e.getMessage)
      }
    })
    source.close()
  }
  def init_DB() : Unit = {
    queries.foreach(query => {
      try {
        session.execute(query)

      } catch {
        case e: Throwable => logger.error(e.getMessage)
      }
    })
  }

  def query_exec(query: String): Unit = {
      session.execute(query)
  }

  def query_exec_result(query: String): Option[ResultSet] = {
    try {
      val res = session.execute(query)
      Some(res)

    } catch {
      case e: Throwable =>
        logger.error(
          "An error occurred while executing a database query : " + e
            .printStackTrace()
        )
        None

    }
  }

  /** Checks the existence of a specified flag in the Cassandra database flag table.
    *
    * @param flag The flag to be checked for existence.
    * @return `true` if the flag exists, `false` otherwise.
    */
  def flag_checker(flag: String): Boolean = {
    logger.info(s"checking $flag flag ...")
    try {
      val queryResult =
        session.execute(s"select * from $keyspace.flags where flag_id='$flag'")
      if (queryResult.iterator().hasNext) {
        true
      } else {
        logger.info(s"$flag flag absent ===> Initializing ... ")
        false
      }
    } catch {
      case e: Throwable =>
        logger.info(s"$flag flag absent ===> Initializing ... ")
        false
    }
  }

  /** Sets the specified flag in the Cassandra database flag table by inserting a new record.
    * This is used after a successful init operation, so we don't waste time initializing data or tables everytime a container is brought down and up again.
    * * @param flag The flag to be set.
    */
  def flag_setter(flag: String): Unit = {
    session.execute(s"insert into $keyspace.flags (flag_id) values ('$flag')")
  }

}
