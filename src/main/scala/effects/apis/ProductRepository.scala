package effects.apis

import domain.models.Product

trait ProductRepository[P[_]] {
  def findAll(): P[Seq[Product]]
}

object ProductRepository {

  import cats.~>

  def apply[F[_]](implicit repo: ProductRepository[F]): ProductRepository[F] = repo
  implicit def toQ[P[_], Q[_]](
                                implicit
                                P: ProductRepository[P],
                                nat: P ~> Q
                              ): ProductRepository[Q] = () => nat(P.findAll())
}
