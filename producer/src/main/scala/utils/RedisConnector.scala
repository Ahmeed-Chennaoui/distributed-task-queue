package utils
import redis.clients.jedis.Jedis
import config.Config.CACHE_URL
object RedisConnector {
  val redis = new Jedis(CACHE_URL,6379)

  def checkTask(task_id:String): Boolean = {
    redis.exists(task_id)
  }
}
