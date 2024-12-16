import cats.effect.unsafe.implicits.global
import cats.effect.{ExitCode, IO, IOApp}
import com.comcast.ip4s.{Host, port}
import domain.usecases.ProductUseCase
import doobie.*
import doobie.free.preparedstatement.PreparedStatementIO
import doobie.implicits.*
import doobie.util.log.{LoggingInfo, Parameters}
import effects.repositories.{InMemoryProductRepository, PostgresProductRepository}
import entrypoints.routes.Routes
import fs2.Stream
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Router

import java.sql.{PreparedStatement, ResultSet}

object MockMain extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {


    implicit val xa: Transactor[IO] = Transactor.fromDriverManager[IO](
      driver = "org.postgresql.Driver",
      url = "jdbc:postgresql:scala",
      user = "admin",
      password = "admin",
      logHandler = None
    )

    //    def findFoo: IO[List[String]] = {
    //      val query = sql"SELECT name FROM foo"
    //        .query[String]
    //      val action = query.to[List]
    //      action.transact(xa)
    //    }
    //
    //
    //    def findFooBy(name: String): Stream[IO, String] = {
    //      val query = "SELECT name FROM foo WHERE name = ?"
    //      val create: ConnectionIO[PreparedStatement] = FC.prepareStatement(query)
    //      val prep: PreparedStatementIO[Unit] = HPS.set(1, name)
    //      val exec: PreparedStatementIO[ResultSet] = FPS.executeQuery
    //
    //      HC.stream[String](create, prep, exec, 100, LoggingInfo("", Parameters.nonBatchEmpty, ""))
    //        .transact(xa)
    //    }
    //
    //    findFooBy("a")
    //      .compile
    //      .toList
    //      .unsafeRunSync()
    //      .foreach(println)
    //    println("----")
    //    findFoo
    //      .unsafeRunSync()
    //      .foreach(println)

    val productRepo = new PostgresProductRepository[IO]
    val productUseCase = new ProductUseCase[IO](productRepo)
    val productRoutes = Routes.testRoutes[IO](productUseCase)

    val h = Host.fromString("localhost").get
    val p = port"8080"
    EmberServerBuilder
      .default[IO]
      .withHost(h)
      .withPort(p)
      .withHttpApp(
        Router(
          "/api" -> productRoutes
        ).orNotFound
      )
      .build
      .useForever
      .as(ExitCode.Success)
  }


}