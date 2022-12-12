import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

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
