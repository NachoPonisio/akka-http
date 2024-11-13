package part1_recap

import scala.concurrent.Future
import scala.util.{Failure, Success}

object ScalaRecap extends App {

    val aCondition: Boolean = false
    def myFunction(x: Int) = {
        if (x > 4) 42 else 65
    }

    // instructions vs expressions
    //instructions are executed in the imperative paradigm vs expressions which are evaluated

    // types plus type inference
    // OO features of Scala
    class Animal

    trait Carnivore {
        def eat(a: Animal): Unit
    }

    object Carnivore

    // generics

    abstract class MyList[+A]

    // method notations -> infix notations
    1 + 2
    1.+(2)

    // FP - apply method lets us treat classes as functions
    val anIncrementer: (Int) => Int = (x: Int) => (x * 1)

    List(1,2,3).map(anIncrementer)

    // HOP - map, flatMap, filter: combinations yield for-comprehensions (chains of map, flatMap and filter)
    // Monads: Pseduo collections that expose map, flatMap and filter (Option, Try)

    // Pattern matching
    val unknown: Any = 2
    val order = unknown match {
        case 1 => "first"
        case 2 => "second"
        case _ => "unknown"
    }

    try {
        throw new RuntimeException()
    } catch { // actually a pattern match{
        case e: Exception => println("I caught one!)
    }


    // multithreading - requires an implicit execution context (thread management)

    import scala.concurrent.ExecutionContext.Implicits.global
    val future = Future {
        // long computation here
        // executed in SOME other thread
        42
    }
    // futures expose map, flatMap, filter + other niceties e.g. recover/recoverWith

    // futures can be monitored for completions

    future.onComplete {
        case Success(value) => println(s"I found the meaning of life: $value")
        case Failure(exception) => println(s"I found $exception while searzching for the meaning of life")
    } //completion runs on SOME thread

    // partial functions

    val partialFunction: PartialFunction[Int, Int] = {
        case 1 => 42
        case 2 => 65
        case _ => 999
    } // if none match => matchError because PF are based on pattern matching

    // type aliases

    type AkkaReceive = PartialFunction[Any, Unit]

    def receive: AkkaReceive = {
        case 1 => println("hello!")
        case _ => println("confused")
    }


    // implicits!
    implicit val timeout = 3000
    def setTimeout(f: () => Unit)(implicit to: Int) = f()
    setTimeout(() => println("hello")) // ohter arg list injected by the compiler. If it finds an Int in scope it will
    // pick up that value

    // conversions
    // 1) implicit methods
    case class Person (name: String) {
        def greet: String = s"Hi, my name is $name"
    }

    implicit def fromStringToPerson(n: String): Person = Person(n)

    "Peter".greet
    // fromStringToPerson("Peter").greet

    // 2) implicit classes
    implicit class Dog(name: String){
        def bark = println("Bark!")
    }

    "Fido".bark
    // new Dog("Fido").bark

    // implicit organizations
    // local scope
    implicit val numberOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)

    println(List(1,2,3).sorted) //(numberOrdering) => List(3,2,1)

    // imported scope, same as futures

    // companion objects of the types involved in the call
    object Person {
        implicit val personOrdering: Ordering[Person] = Ordering.fromLessThan((a,b) => a.name.compareTo(b.name) < 0)
    }

    println(List(Person("Bob"), Person("Alice")).sorted)
        


}
