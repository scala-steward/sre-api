package sre.dashboard

import org.http4s.Uri
import io.circe._
import io.circe.generic.auto._
import io.circe.config.syntax._

case class TrainSettings(endpoint: Uri)

case class TransportSettings(train: TrainSettings)

case class FinanceSettings(db: String)

case class Settings(transport: TransportSettings, finance: FinanceSettings)

object Settings {

  val CONFIG_FILE_NAME = "app.conf"

  lazy val AppConfig: com.typesafe.config.Config =
    com.typesafe.config.ConfigFactory.parseResources(CONFIG_FILE_NAME)

  def load(): Either[Error, Settings] = {
    for {
      trainSettings <- AppConfig.as[TrainSettings]("transport.train")
      financeSettings <- AppConfig.as[FinanceSettings]("finance")
      transportSettings = TransportSettings(trainSettings)
    } yield Settings(transportSettings, financeSettings)
  }

  implicit val UriDecoder: Decoder[Uri] = new Decoder[Uri] {
    final def apply(c: HCursor): Decoder.Result[Uri] =
      c.as[String].right.flatMap { s =>
        Uri.fromString(s).left.map { error =>
          DecodingFailure(error.message, c.history)
        }
      }
  }
}
