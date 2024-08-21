package utils

import com.datastax.oss.driver.api.core.cql.{ResultSet, Row}
import scala.annotation.tailrec
import scala.jdk.CollectionConverters.IteratorHasAsScala

object ResultSetManipulator {
  /**
   * Converts a ResultSet to a List of type T using a specified extraction function.
   *
   * @param resultSet     The ResultSet to be converted.
   * @param extractData The extraction function for initializing instances of type T from a Row.
   *                    The function should extract parameters from the Row to construct an instance of type T.
   * @tparam T            The type of elements in the resulting List.
   * @return              A List of elements of type T extracted from the ResultSet.
   */
  def resultSetToList[T](resultSet: ResultSet, extractData: Row => T): List[T] = {
    /**
     * tail recursive function that accumulate elements from the ResultSet iterator.
     */
    @tailrec
    def resultSetToListAcc(acc: List[T], iterator: Iterator[Row]): List[T] = {
      if (iterator.hasNext) {
        val row = iterator.next()
        resultSetToListAcc(acc :+ extractData(row), iterator)
      } else {
        acc
      }
    }

    resultSetToListAcc(List(), resultSet.iterator().asScala)
  }
}
