# Talking points

## Main topics

For comprehensions
Try/Catch
Option
Either
Future fail
EitherT maybe

## For comprehensions

### Same type all the way

- List
- Try
- Option
- Either
- Future
- EitherT

### Aborts on first failure

Failure depends on type.

- `Option` `None`
- `Either` `Left`
- `Future` `Fail`
- `EitherT` `LeftT`

For comprehension instead of nested `flatMap`s with the `yield` being a `map`
Make example more clear

```scala
l1.flatMap(el1 => l2.map(el2 => el1 + el2))
for {
  el1 <- l1
  el2 <- l2
} yield el1 + el2
```

## Try/Catch

`Try[T]`
use `Try` instead of `try`.
Returns `Success` or `Failure`. `Failure` always has an `Exception`
Can be used in for comprehension

## Option

`Option[T]`
To signal a failure/not found without giving any special error message.
`Some` for a value and `None` for missing value

```Scala
def getUserById(id: Int): User = ??? // What happens if the user is not found
def getUserById(id: Int): Option[User] // Clear from signature what the results can be
```

## Either

`Either[Error, Result]` has left and right side. Is right biased - right is the success value
The error does not have to inherit from throwable
`Right` for positive result and `Left` for error result.
`Try` and `Option` can be made into Either

### Catch only

```Scala
import scala.util.Either

val input = "123"
val result = Either.catchOnly[NumberFormatException](input.toInt)

result match {
  case Right(n) => println(n)  // prints 123
  case Left(e) => println(e)  // prints java.lang.NumberFormatException
}
```

### Cats

Cats helpers. Can help guide type inference compared to the Left, Right construct

```Scala
import cats.syntax.all.*

val e1 = 5.asRight[String]
// e1: Either[String, Int] = Right(value = 5)
val e2 = "error".asLeft[Int]
// e2: Either[String, Int] = Left(value = "error")
```

## Future

`Future[T]`
Has `Success` and `Failure` and failure is always a throwable

Several downsides of Future. Is not referentially transparent, executes whenever it is in context. Execution context has to be passed around a lot or chosen further down in the code. Can lead to different execution context for the same process. Same function could be used from an async job and a synchronous api.

Should be executed as far out as possible.

Make example where you have a future with an Either as the success result. Requires boilerplate to handle.

## EitherT

`EitherT[IO, Error, Result]` The IO can be Option, Either, Future.
Can be created from the IO types that it supports.

Helps with the problem of `Future` containing an Either.

Different IOs can be mixed together in the same for comprehension when wrapped in EitherT

```scala
for {
    user <- EitherT(findUserByName(userName))
    _ <- EitherT(checkPassword(user, password))
    _ <- EitherT(checkSubscription(user))
    _ <- EitherT(checkUserStatus(user))
  } yield user
```

Make exmaples of `Future[Either[L, R]]`

## Exercise

```Scala
def authenticate(userName: String, password: String): EitherT[IO, AuthenticationError, User] =
  for {
    user <- EitherT(findUserByName(userName))
    _ <- EitherT(checkPassword(user, password))
    _ <- EitherT(checkLicence(user, LocalDate.now()))
    _ <- EitherT(checkUserStatus(user))
  } yield user
}
```

### Idea

Have several validation functions that throw different kind of validation errors.

- Some of them you own and can change
- Some of them are owned by others and can't be changed
- Some have Future in them that is executed within the function

1. Change method signatures so that the intent of the function is clear. There should be no need to look at the implementation when you want to use the function. Return a clear error message to the user.
  For the moment, leave the execution of the Future in the functions.

2. Move the effect execution to the outer edge.
  Suggest looking at the cats EitherT documentation for inspiration.
