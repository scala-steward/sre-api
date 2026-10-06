val projectName = "sre-api"

val ScalaVersion = "2.13.8"
val Fs2Version = "3.8.0"
val Http4sVersion = "1.0.0-M34"
val CirceVersion = "0.14.17"
val LogbackVersion = "1.4.8"
val CirceConfigVersion = "0.10.0"
val AnormVersion = "2.6.8"
val SqliteJdbcVersion = "3.42.0.0"
val Ofx4jVersion = "1.35"
val JsoupVersion = "1.16.1"
val AwsSdkVersion = "1.11.615"
val ScalaTestVersion = "3.2.16"
val ScalaMockVersion = "4.4.0"
val CatsEffectTestScalaTestVersion = "1.5.0"
val EasyMockVersion = "4.2"
val JavaWebSocketVersion = "1.5.1"
val FeralVersion = "1.0.0-M4"
val Log4catsVersion = "2.6.0"
val DeclineVersion = "2.4.1"

val gitVersion = {
  import scala.sys.process._
  val GitShortVersion = """^.*([0-9abcdef]{7})$""".r
  Option("git describe --abbrev=7 --always" !!)
    .map(_.trim.toLowerCase)
    .map {
      case GitShortVersion(gitVersion) => gitVersion
      case other                       => other
    }
    .filterNot(_.isEmpty)
    .getOrElse("x-SNAPSHOT")
}

scalacOptions ++= Seq("-Xlint")
Test / logBuffered := false

lazy val serverProject = (project in file("server"))
  .settings(
    organization := "sre",
    name := projectName,
    version := "0.0.1-SNAPSHOT",
    scalaVersion := ScalaVersion,
    libraryDependencies ++= Seq(
      "co.fs2" %% "fs2-core" % Fs2Version,
      "co.fs2" %% "fs2-io" % Fs2Version,
      "org.http4s" %% "http4s-dsl" % Http4sVersion,
      "org.http4s" %% "http4s-ember-server" % Http4sVersion,
      "org.http4s" %% "http4s-ember-client" % Http4sVersion,
      "org.http4s" %% "http4s-circe" % Http4sVersion,
      "org.http4s" %% "http4s-scala-xml" % Http4sVersion,
      "ch.qos.logback" % "logback-classic" % LogbackVersion,
      "io.circe" %% "circe-parser" % CirceVersion,
      "io.circe" %% "circe-generic" % CirceVersion,
      "io.circe" %% "circe-literal" % CirceVersion,
      "io.circe" %% "circe-config" % CirceConfigVersion,
      "org.xerial" % "sqlite-jdbc" % SqliteJdbcVersion,
      "org.playframework.anorm" %% "anorm" % AnormVersion,
      "com.webcohesion.ofx4j" % "ofx4j" % Ofx4jVersion,
      "com.amazonaws" % "aws-java-sdk" % AwsSdkVersion,
      "org.java-websocket" % "Java-WebSocket" % JavaWebSocketVersion,
      "org.scalatest" %% "scalatest" % ScalaTestVersion % "test",
      "org.easymock" % "easymock" % EasyMockVersion % "test",
      "org.typelevel" %% "cats-effect-testing-scalatest" % CatsEffectTestScalaTestVersion % "test"
    ),
    addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
    addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1")
  )
  .settings(
    assembly / mainClass := Some("sre.api.Server"),
    assembly / assemblyMergeStrategy := {
      case PathList("META-INF", xs @ _*) => MergeStrategy.discard
      case x                             => MergeStrategy.first
    },
    assembly / assemblyJarName := s"$projectName.jar"
  )
  .dependsOn(commons)

scalacOptions ++= Seq("-Xlint:-byname-implicit")

lazy val commons = (project in file("commons"))
  .settings(
    organization := "sre",
    name := "commons",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := ScalaVersion,
    libraryDependencies ++= Seq(
      "org.scala-lang.modules" %% "scala-java8-compat" % "1.0.2",
      "software.amazon.awssdk" % "s3" % "2.20.92",
      "io.circe" %% "circe-parser" % CirceVersion,
      "io.circe" %% "circe-generic" % CirceVersion,
      "org.http4s" %% "http4s-dsl" % Http4sVersion,
      "org.http4s" %% "http4s-ember-client" % Http4sVersion,
      "org.http4s" %% "http4s-circe" % Http4sVersion,
      "org.java-websocket" % "Java-WebSocket" % JavaWebSocketVersion
    ),
    addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1")
  )

lazy val heatersProject = (project in file("heaters-api"))
  .settings(
    organization := "sre",
    name := "heaters-api",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := ScalaVersion,
    libraryDependencies ++= Seq(
      "org.typelevel" %% "feral-lambda" % FeralVersion,
      "org.typelevel" %% "feral-lambda-http4s" % FeralVersion,
      "org.http4s" %% "http4s-scala-xml" % Http4sVersion,
      "org.http4s" %% "http4s-dsl" % Http4sVersion,
      "org.http4s" %% "http4s-circe" % Http4sVersion,
      "org.http4s" %% "http4s-ember-client" % Http4sVersion,
      "io.circe" %% "circe-parser" % CirceVersion,
      "io.circe" %% "circe-generic" % CirceVersion,
      "io.circe" %% "circe-literal" % CirceVersion,
      "ch.qos.logback" % "logback-classic" % LogbackVersion
    ),
    addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1")
  )
  .settings(
    assembly / assemblyJarName := s"heaters-api.jar",
    assembly / assemblyMergeStrategy := {
      case PathList("META-INF", xs @ _*) => MergeStrategy.discard
      case x                             => MergeStrategy.first
    }
  )
  .dependsOn(commons)

lazy val shuttersProject = (project in file("shutters-api"))
  .settings(
    organization := "sre",
    name := "shutters-api",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := ScalaVersion,
    libraryDependencies ++= Seq(
      "org.typelevel" %% "feral-lambda" % FeralVersion,
      "org.typelevel" %% "feral-lambda-http4s" % FeralVersion,
      "org.http4s" %% "http4s-scala-xml" % Http4sVersion,
      "org.http4s" %% "http4s-dsl" % Http4sVersion,
      "org.http4s" %% "http4s-circe" % Http4sVersion,
      "org.http4s" %% "http4s-ember-client" % Http4sVersion,
      "io.circe" %% "circe-parser" % CirceVersion,
      "io.circe" %% "circe-generic" % CirceVersion,
      "io.circe" %% "circe-literal" % CirceVersion,
      "ch.qos.logback" % "logback-classic" % LogbackVersion
    ),
    addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1")
  )
  .settings(
    assembly / assemblyJarName := s"shutters-api.jar",
    assembly / assemblyMergeStrategy := {
      case PathList("META-INF", xs @ _*) => MergeStrategy.discard
      case x                             => MergeStrategy.first
    }
  )
  .dependsOn(commons)

lazy val financeProject = (project in file("finance-api"))
  .settings(
    organization := "sre",
    name := "finance-api",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := ScalaVersion,
    libraryDependencies ++= Seq(
      "org.typelevel" %% "feral-lambda" % FeralVersion,
      "org.typelevel" %% "feral-lambda-http4s" % FeralVersion,
      "org.http4s" %% "http4s-scala-xml" % Http4sVersion,
      "org.http4s" %% "http4s-dsl" % Http4sVersion,
      "org.http4s" %% "http4s-circe" % Http4sVersion,
      "org.http4s" %% "http4s-ember-client" % Http4sVersion,
      "io.circe" %% "circe-parser" % CirceVersion,
      "io.circe" %% "circe-generic" % CirceVersion,
      "io.circe" %% "circe-literal" % CirceVersion,
      "ch.qos.logback" % "logback-classic" % LogbackVersion,
      "org.playframework.anorm" %% "anorm" % AnormVersion,
      "org.jsoup" % "jsoup" % JsoupVersion,
      "com.webcohesion.ofx4j" % "ofx4j" % Ofx4jVersion,
      "org.xerial" % "sqlite-jdbc" % SqliteJdbcVersion,
      "org.typelevel" %% "log4cats-core" % Log4catsVersion,
      "org.typelevel" %% "log4cats-slf4j" % Log4catsVersion,
      "org.scalatest" %% "scalatest" % ScalaTestVersion % "test",
      "org.typelevel" %% "cats-effect-testing-scalatest" % CatsEffectTestScalaTestVersion % "test"
    ),
    addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1")
  )
  .settings(
    assembly / assemblyJarName := s"finance-api.jar",
    assembly / assemblyMergeStrategy := {
      case PathList("META-INF", xs @ _*) => MergeStrategy.discard
      case x                             => MergeStrategy.first
    }
  )
  .dependsOn(commons)

lazy val cliProject = (project in file("cli"))
  .settings(
    organization := "sre",
    name := "cli",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := ScalaVersion,
    libraryDependencies ++= Seq(
      "com.monovore" %% "decline" % DeclineVersion,
      "com.monovore" %% "decline-effect" % DeclineVersion,
      "io.circe" %% "circe-parser" % CirceVersion,
      "io.circe" %% "circe-config" % CirceConfigVersion,
      "org.http4s" %% "http4s-ember-client" % Http4sVersion
    )
  )
  .settings(
    assembly / assemblyJarName := s"cli.jar",
    assembly / assemblyMergeStrategy := {
      case PathList("META-INF", xs @ _*) => MergeStrategy.discard
      case x                             => MergeStrategy.first
    }
  )
  .dependsOn(financeProject, heatersProject, shuttersProject)
