package effects.repositories

import cats.Monad
import cats.effect.IO
import domain.models
import domain.models.Product
import doobie.free.connection.ConnectionIO
import doobie.free.preparedstatement.PreparedStatementIO
import doobie.implicits.*
import doobie.util.log.{LoggingInfo, Parameters}
import doobie.*
import effects.apis.ProductRepository
import fs2.Stream

import java.sql.{PreparedStatement, ResultSet}

class PostgresProductRepository[F[_] : Monad](implicit xa: Transactor[IO]) extends ProductRepository[IO] {
  override def findAll(): IO[Seq[models.Product]] = {
    findFooBy()
  }

  private def findFooBy()(implicit xa: Transactor[IO]): IO[List[Product]] = {
    sql"SELECT id, name, price FROM products"
      .query[Product]
      .to[List]
      .transact(xa)
  }

}