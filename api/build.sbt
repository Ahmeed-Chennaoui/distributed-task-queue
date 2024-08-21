ThisBuild / version := "0.15.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.13"
enablePlugins(JavaAppPackaging)
//enablePlugins(ScoverageSbtPlugin)
enablePlugins(JacocoPlugin)
//coverageEnabled := true
jacocoReportSettings := JacocoReportSettings().withFormats(
  JacocoReportFormats.XML
)

sonarProperties := Map(
  "sonar.projectKey" -> "distributed_task_queue_api",
  "sonar.sources" -> "src/main/scala",
  "sonar.host.url" -> "http://localhost:9000",
  "sonar.login" -> "sqp_13a1721a7d47abc24b5651ce1d442e4aafcf7d3b",
  "sonar.java.coveragePlugin" -> "jacoco",
  "sonar.coverage.jacoco.xmlReportPaths" -> "target/scala-2.13/jacoco/report/jacoco.xml"
)
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % "10.5.0",
  "com.typesafe.akka" %% "akka-actor" % "2.8.0",
  "com.typesafe.akka" %% "akka-stream" % "2.8.0",
  "org.apache.kafka" %% "kafka" % "3.7.0",
  "org.slf4j" % "slf4j-api" % "2.0.9",
  "ch.qos.logback" % "logback-classic" % "1.5.2",
  "com.datastax.oss" % "java-driver-core" % "4.15.0",
  "ch.megard" %% "akka-http-cors" % "1.2.0",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.5.0",
  "com.github.nscala-time" %% "nscala-time" % "2.32.0",
  "org.scalatest" %% "scalatest" % "3.2.17" % "test",
  "org.cassandraunit" % "cassandra-unit" % "4.3.1.0" % "test",
  "com.typesafe.akka" %% "akka-testkit" % "2.8.0" % Test,
  "com.typesafe.akka" %% "akka-http-testkit" % "10.5.0" % Test,
  "io.prometheus" % "prometheus-metrics-core" % "1.0.0",
  "io.prometheus" % "prometheus-metrics-instrumentation-jvm" % "1.0.0",
  "io.prometheus" % "simpleclient" % "0.16.0",
  "io.prometheus" % "simpleclient_common" % "0.16.0",
  "io.prometheus" % "simpleclient_hotspot" % "0.16.0",
  "io.prometheus" % "simpleclient_httpserver" % "0.16.0"
)
lazy val root = (project in file("."))
  .settings(
    name := "API"
  )
