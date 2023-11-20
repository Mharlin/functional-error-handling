# Exercises for functional scala workshop

Have the `exercise` folder as root when opening the project in IntelliJ or VSCode.

## Exercise 1

Do the exercise in `src/main/scala/Customer.scala`

- Create a Order class that has a list of Products.
- Create a Product class that has: Name, Price, Category.
- Add a list of orders to the Customer.
- If a customer has order value that is above 1000 it should be a VIP user (See the commented out tests for inspiration).

## Exercise 2

Do the exercise in `src/main/scala/ErrorHandling.scala`

1. Change method signatures so that the intent of the function is clear.
  There should be no need to look at the implementation when you want to use the function.  
  Return a clear error message to the user by using Either.
  For the moment, leave the execution of the Future in the functions.  
  
2. Move the effect execution to the outer edge.
  Suggest looking at the cats EitherT documentation for inspiration.
