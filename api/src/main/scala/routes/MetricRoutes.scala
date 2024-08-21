package routes
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.{
  ContentTypes,
  HttpEntity,
  HttpResponse,
  StatusCodes
}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import controllers.MetricControllers.updateMetrics

import scala.concurrent.ExecutionContext.Implicits.global
import java.io.StringWriter
import io.prometheus.client.exporter.common.TextFormat
import io.prometheus.client.CollectorRegistry

import scala.concurrent.{Future, Await}
import scala.concurrent.duration.Duration

object MetricRoutes {
  val getMetrics: Route = (get & path("metrics")) {
    Await.ready(
      Future {
        updateMetrics()
      },
      Duration.Inf
    )
    val writer = new StringWriter()
    TextFormat.write004(
      writer,
      CollectorRegistry.defaultRegistry.metricFamilySamples()
    )
    complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, writer.toString))
  }
}
