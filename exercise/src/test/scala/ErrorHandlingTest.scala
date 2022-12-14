import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import cats.data.EitherT
import cats.implicits.catsStdInstancesForEither
import java.util.concurrent.Future
import scala.util.Try

class ErrorHandlingTest extends AnyFunSuite with Matchers {
  test("authenticateUser licence expired") {
    ErrorHandling.authenticateUser("Magnus", "magnus", "Niovi") shouldBe User("licence expired", "licence expired", true)
  }

  test("authenticateUser user is not active") {
    ErrorHandling.authenticateUser("devel", "devel", "Acme") shouldBe User("licence expired", "licence expired", true)
  }
    
  test("authenticateUser success") {
    ErrorHandling.authenticateUser("Elin", "elin", "RF") shouldBe User("Elin", "elin", true)
  }

  test("authenticateUser wrong company") {
    ErrorHandling.authenticateUser("Helena", "helena", "wrong company") shouldBe User("Elin", "elin", true)
  }
}

  val a: EitherT[Try, String, Unit] = EitherT.fromEither(testP("Elin", "elin"))

  def testP(userName: String, password: String): Either[String, Unit] = {
    if (userName == "Elin" && password == "elin") Right(())
    else Left("Wrong password")
  }