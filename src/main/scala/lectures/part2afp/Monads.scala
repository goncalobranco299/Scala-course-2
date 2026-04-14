package lectures.part2afp

object Monads extends App {

  // our own Try monad

  trait Attempt[+A] {
    def flatMap[B](f: A => Attempt[B]): Attempt[B]
  }
  object Attempt {
    def apply[A](a: => A): Attempt[A] =
      try {
        Success(a)
      } catch {
        case e: Throwable => Fail(e)
      }
  }

  case class Success[+A](value: A) extends Attempt[A] {
    def flatMap[B](f: A => Attempt[B]): Attempt[B] =
      try {
        f(value)
      } catch {
        case e: Throwable => Fail(e)
      }
  }

  case class Fail(e: Throwable) extends Attempt[Nothing] {
    def flatMap[B](f: Nothing => Attempt[B]): Attempt[B] = this
  }

  val attempt = Attempt {
    throw new RuntimeException("My own monad, yes!")
  }

  println(attempt)

  // 1 - Lazy monad
  class Lazy[+A](value: => A) {
    // call by need
    def flatMap[B](f: (=> A) => Lazy[B]): Lazy[B] = f(value)
  }
  object Lazy {
    def apply[A](value: => A): Lazy[A] = new Lazy(value)
  }

  val LazyInstance = Lazy {
    println("Today I don't feel like doing anything")
    42
  }
  val flatMappedInstance = LazyInstance.flatMap(x => Lazy {
    10 * x
  })
  val flatMappedInstance2 = LazyInstance.flatMap(x => Lazy {
    10 * x
  })
  /*
    left-identity
    unit.flatMao(f) = f(v)
    Lazy(v).flatMao(f) = f(v)

    right-identity
    l.flatMap(unit) = 1
    Lazy(v).flaMap(x => Lazy(x)) = Lazy(v)

    associativity: 1.flatMap(f).flatMap(g)  = 1.flatMap(x => f(x).flatMap(g))

    Lazy(v).flatMap(f).flatMap(g) = f(v).flatMap(g)
    Lazy(v).flatMap(x => f(x).flatMap(g) = f(v).flatMap(g)

    // 2: map and flatten in terms of flatMap
    /*
      Monad[T] {  //  list
        def flatMap[B](f: T => B):  Modad[B]  = flatMap(x => unit(f(x))) // Monad[B]
        def flatten(m: Monad[Monad[T]]): Monad[T] = m.flatMap((x: Monad[T] => x)

        List(1,2,3).map(_ * 2) = List(1,2,3).flatMap(x => List(x * 2))
        List(List(1,2), List(3,4)).flatten = List(List(1,2), Lit(3,4)).flatMap(x => x)  = List(1,2,3,4)
   */
   */
   */
   */
   */

   */
   */


   */
}
