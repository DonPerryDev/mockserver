package domain.usecases

import cats.Monad
import domain.models.Product
import effects.apis.ProductRepository

class ProductUseCase[F[_]: Monad](repository: ProductRepository[F]) {
  def listAllProducts(): F[Seq[Product]] = repository.findAll()
}