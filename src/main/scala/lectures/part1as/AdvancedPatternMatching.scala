package lectures.part1as

object AdvancedPatternMatching extends App {

  val numbers = List(1)
  val description = numbers match {
    case head :: Nil => println(s"he only elemnt is $head")
    case _ =>
  }
  /*
    - constants
    - wildcards
    - case classes
    - tuples
    - some especial magic like above
   */

  class Person(val name: String, val age: Int)

  object Person {
    def unapply(person: Person): Option[(String, Int)] =
      if(person.age < 21) None
      else Some ((person.name, person.age))

    def unapply(age: Int): Option[String] =
      Some(if(age < 21) "minor" else "major")
  }

  val bob = new Person(name = "Bob", age = 25)
  val greeting = bob match {
    case Person(n,a) => s"Hi, my name is $n and I am $a yo"
  }

  println(greeting)

  val legalStatus = bob.age match {
    case Person(status) => s"My legal staus is $status"
  }
  println(legalStatus)

  /*
    Exercice.
   */

  object even {
    def unapply(arg: Int): Boolean = arg % 2 == 0
  }

  object singleDigit {
    def unapply(arg: Int): Boolean = arg > -10 && arg < 10
  }


  val n: Int = 45
  val mathProperty = n match {
    case singleDigit => "single digit"
    case even => "an ever number"
    case _ => "no property"
  }

  println(mathProperty)

  // infix patterns
  case class Or[A,B](a: A,b: B) // Either
  val either = Or(2, "two")
  val humanDescritpion = either match {
    case number Or string => s"$number is written as $string"
  }
  println(humanDescritpion)

  // decomposing sequences
  val varag = numbers match {
    case List(1, _*) => "starting with 1"
  }

  abstract class MyList[+A] {
    def head: A = ???
    def tail: MyList[A] = ???
  }
  case object Empty extends MyList[Nothing]
  case class Cons[+A](override val head: A, override val tail: MyList[A]) extends MyList[A]

  object  Mylist {
    def unapplySeq[A](list: MyList[A]): Option[Seq[A]] =
      if (list == Empty) Some(Seq.empty)
      else unapplySeq(list.tail).map(list.head +: _)
  }

  val myList: MyList[Int] = Cons(1, Cons(2, Cons(3, Empty)))
  val decomposed = Mylist match {
    case Mylist(1,2,_*) => "Starting wit 1,2 "
    case _ => "something else"
  }
  println(decomposed)

  // custom return types for unapply
  // isEmpty: Boolean, get: something.

  abstract class Wrapper[T] {
    def isEmpty: Boolean
    def get: T
  }

  object PersonWrapper {
    def unapply(person: Person): Wrapper[String] = new Wrapper[String] {
      def isEmpty = false
      def get: String = person.name
    }
  }

  println(bob match {
    case PersonWrapper(n) => s"This person´s name is $n"
    case _ => "An alien"
  })

}
