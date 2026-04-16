package exercises

import lectures.part4implicits.TypeClasses.User

object EqualityPlayground extends App {

/**
 * Equality
 */
trait Equal[T] {
  def apply(a: T, b: T): Boolean
}

implicit object NameEquality extends Equal[User] {
  override def apply(a: User, b: User): Boolean = a.name == b.name
}

object FullEquality extends Equal[User] {
  override def apply(a: User, b: User): Boolean = a.name == b.name && a.email == b.email
  }

  /*
    Exercice: implement the TC pattern for the Equality tc.
   */
  object Equal {
    def apply[T](a: T, b: T)(implicit equalizer: Equal[T]): Boolean =
      equalizer.apply(a,b)
  }
  val john = User("John", 32, "john@rockthejvm.com")
  val anotherJohn =  User("John", 45, "anotherJohn@rockthejvm.com")
  println(Equal(john,anotherJohn))
  // AD-HOC polymorphism

  implicit class TypeSafeEqual[T](value: T) {
    def ===(other: T)(implicit equalizer: Equal[T]): Boolean = equalizer.apply(value,other)
    def !==(other: T)(implicit equalizer: Equal[T]): Boolean = equalizer.apply(value,other)

  }

  println(john === anotherJohn)
  /*
    john.===(anotherJohn)
    new TypeSafeEqual[User](john).===(anotherJohn)
    new TypeSafeEqual[User](john).===(anotherJohn)(NameEquality)
   */

  println(john == 43)
  //  println(john === 43) // TYPE SAFE

}
