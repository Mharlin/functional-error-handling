import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.concurrent.duration.Duration.apply
import scala.concurrent.duration.Duration
import java.time.LocalDate

object ErrorHandling extends App {
  authenticateUser("Susanna", "susanna", "RF")

  def authenticateUser(username: String, password: String, company: String): User = {
    val user = findUserByName(username)
    checkPassword(user, password)
    checkLicence(user, LocalDate.now())
    checkUserStatus(user)
    checkUserBelongsToCompany(user, company)
    user
  }

  def findUserByName(username: String): User = {
    val user = Await.result(Database.users.map(_.find(_.username == username)), Duration("5 seconds"))

    if (user.isDefined) then user.get
    else throw new IllegalArgumentException("User does not exist")
  }

  def checkPassword(user: User, password: String): Unit =
    if user.password == password then ()
    else throw new IllegalAccessException("Wrong password")

  def checkUserStatus(user: User): Unit =
    if user.isActivated == true then ()
    else throw new Exception("User is not active")

  def checkLicence(user: User, date: LocalDate): Unit = {
    val licence = Await.result(Database.licences.map(_.find(_.username == user.username)), Duration("5 seconds"))
    if licence.isDefined then
      val expiryDate = LocalDate.parse(licence.get.expiryDate)
      if expiryDate.isAfter(date) then ()
      else throw new Exception("Licence expired")
    else throw new Exception("Licence does not exist")
  }
  
  // This method is owned by another team and cannot be changed
  def checkUserBelongsToCompany(user: User, company: String): Unit = {
    val userCompany = Await.result(Database.companies.map(_.find(_.username == user.username)), Duration("5 seconds"))
    if userCompany.isDefined then
      if userCompany.get.company == company then ()
      else throw new IllegalCallerException("User does not belong to company")
    else throw new IllegalCallerException("User does not belong to company")
  }
}

case class User(username: String, password: String, isActivated: Boolean)
case class Licence(username: String, expiryDate: String)
case class Company(username: String, company: String)

object Database {
  def users = Future(
    Seq(
      User("devel", "devel", false),
      User("Elin", "elin", true),
      User("Erica", "erica", true),
      User("Helena", "helena", true),
      User("Jasmina", "jasmina", true),
      User("Magnus", "magnus", true),
      User("Susanna", "susanna", true)
    )
  )

  def licences = Future(
    Seq(
      Licence("devel", "2021-01-01"),
      Licence("Elin", "2025-01-01"),
      Licence("Erica", "2023-01-01"),
      Licence("Helena", "2023-01-01"),
      Licence("Jasmina", "2023-01-01"),
      Licence("Magnus", "2021-12-31"),
      Licence("Susanna", "2024-01-01")
    )
  )
  
  def companies = Future(
    Seq(
      Company("devel", "Acme"),
      Company("Elin", "RF"),
      Company("Erica", "RF"),
      Company("Helena", "RF"),
      Company("Jasmina", "RF"),
      Company("Magnus", "Niovi"),
      Company("Susanna", "RF"),
    )
  )
}
