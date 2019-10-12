name := "catalogue-service"

version := "0.1"

scalaVersion := "2.12.5"

enablePlugins(JavaServerAppPackaging)

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.12",
  "com.typesafe.akka" %% "akka-http" % "10.1.1",
  "com.typesafe.play" %% "play-json" % "2.7.3",
  "de.heikoseeberger" %% "akka-http-play-json" % "1.29.1",
  "com.typesafe.akka" %% "akka-stream" % "2.5.12",
  "com.typesafe.slick" %% "slick" % "3.2.3",
  "com.h2database" % "h2" % "1.4.192",
  "org.postgresql" % "postgresql" % "42.2.8",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.2.3",
  "com.typesafe" % "config" % "1.3.2",
  "org.scalatest" %% "scalatest" % "3.0.5" % Test,
  "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.12" % Test,
  "com.typesafe.akka" %% "akka-testkit" % "2.5.12" % Test,
  "com.typesafe.akka" %% "akka-http-testkit" % "10.1.1" % Test

)

