package effects.repositories

import cats.Monad
import domain.models
import domain.models.Product
import effects.apis.ProductRepository

class InMemoryProductRepository[F[_]: Monad] extends ProductRepository[F] {
  private val products = Seq(
    Product(1, "Product A", 10.0),
    Product(2, "Product B", 20.0)
  )
  override def findAll(): F[Seq[models.Product]] = Monad[F].pure(products)
}
