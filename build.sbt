val projectName = "sre-api"

val Fs2Version = "2.5.10"
val Http4sVersion = "0.21.31"
val LogbackVersion = "1.4.1"
val CirceVersion = "0.13.0"
val CirceConfigVersion = "0.8.0"
val AnormVersion = "2.6.8"
val SqliteJdbcVersion = "3.32.3.3"
val Ofx4jVersion = "1.28"
val JsoupVersion = "1.12.2"
val ScalaCacheVersion = "0.28.0"
val ScalaCacheCatsVersion = "0.28.0"
val AwsSdkVersion = "1.11.615"
val ScalaTestVersion = "3.2.11"
val ScalaMockVersion = "4.4.0"
val CatsEffectTestScalaTestVersion = "0.4.2"
val EasyMockVersion = "4.2"
val JavaWebSocketVersion = "1.5.1"

val gitVersion = {
  import scala.sys.process._
  val GitShortVersion = """^.*([0-9abcdef]{7})$""".r
  Option("git describe --abbrev=7 --always" !!).map(_.trim.toLowerCase).map {
    case GitShortVersion(gitVersion) => gitVersion
    case other                       => other
  }.filterNot(_.isEmpty).getOrElse("x-SNAPSHOT")
}

lazy val root = (project in file("."))
  .settings(
    organization := "sre",
    name := projectName,
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.13.8",
    libraryDependencies ++= Seq(
      "co.fs2"                    %% "fs2-core"                       % Fs2Version,
      "co.fs2"                    %% "fs2-io"                         % Fs2Version,
      "org.http4s"                %% "http4s-dsl"                     % Http4sVersion,
      "org.http4s"                %% "http4s-blaze-server"            % Http4sVersion,
      "org.http4s"                %% "http4s-blaze-client"            % Http4sVersion,
      "org.http4s"                %% "http4s-circe"                   % Http4sVersion,
      "org.http4s"                %% "http4s-scala-xml"               % Http4sVersion,
      "ch.qos.logback"            %  "logback-classic"                % LogbackVersion,
      "io.circe"                  %% "circe-parser"                   % CirceVersion,
      "io.circe"                  %% "circe-generic"                  % CirceVersion,
      "io.circe"                  %% "circe-literal"                  % CirceVersion,
      "io.circe"                  %% "circe-config"                   % CirceConfigVersion,
      "org.xerial"                % "sqlite-jdbc"                     % SqliteJdbcVersion,
      "org.playframework.anorm"   %% "anorm"                          % AnormVersion,
      "com.webcohesion.ofx4j"     % "ofx4j"                           % Ofx4jVersion,
      "org.jsoup"                 % "jsoup"                           % JsoupVersion,
      "com.github.cb372"          %% "scalacache-guava"               % ScalaCacheVersion,
      "com.github.cb372"          %% "scalacache-cats-effect"         % ScalaCacheCatsVersion,
      "com.amazonaws"             % "aws-java-sdk"                    % AwsSdkVersion,
      "org.java-websocket"        % "Java-WebSocket"                  % JavaWebSocketVersion,
      "org.scalatest"             %% "scalatest"                      % ScalaTestVersion % "test",
      "org.easymock"              % "easymock"                        % EasyMockVersion % "test",
      "com.codecommit"            %% "cats-effect-testing-scalatest"  % CatsEffectTestScalaTestVersion % "test"
    ),
    addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.10.3"),
    addCompilerPlugin("com.olegpy"     %% "better-monadic-for" % "0.3.1")
  ).
  settings(
    mainClass in assembly := Some("sre.api.Server"),
    assemblyMergeStrategy in assembly := {
      case PathList("META-INF", xs @ _*) => MergeStrategy.discard
      case x => MergeStrategy.first
    },
    assemblyJarName in assembly := s"$projectName.jar",
  )

scalacOptions ++= Seq("-Xlint")

// ScalaTest
logBuffered in Test := false
