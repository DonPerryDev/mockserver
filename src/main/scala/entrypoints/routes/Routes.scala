package entrypoints.routes

import cats.effect.Concurrent
import cats.implicits.*
import domain.models.Product
import domain.usecases.ProductUseCase
import io.circe.generic.semiauto.*
import io.circe.{Decoder, Encoder}
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.circe.CirceEntityEncoder._

object Routes {
  def testRoutes[F[_] : Concurrent](productUseCase: ProductUseCase[F]) = {
    val dsl = Http4sDsl[F]
    import dsl.*

    implicit val productDecoder: Decoder[Product] = deriveDecoder[Product]
    implicit val productEncoder: Encoder[Product] = deriveEncoder[Product]
    implicit val productsEncoder: Encoder[Seq[Product]] = Encoder.encodeSeq[Product]

    val a: Option[Product] = None

    HttpRoutes.of[F] {
      case GET -> Root / "test" =>
        productUseCase.listAllProducts()
          .flatMap(products => Ok(products)
          )
    }
  }
}
