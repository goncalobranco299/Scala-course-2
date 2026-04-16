package lectures.part4implicits

object PimpMyLibrary extends App {

  // 2.isPrime

  implicit class RichInt(val value: Int) extends AnyVal {
    def isEven: Boolean = value % 2 == 0
    def sqrt: Double = Math.sqrt(value)

    def times(function: () => Unit): Unit = {
      def timesAux(n: Int): Unit =
        if(n <= 0) ()
        else {
          function()
          timesAux(n - 1)
        }
      timesAux(value)
    }

    def *[T](list: List[T]): List[T] = {
      def concatenate(n: Int): List[T] =
        if(n <= 0) List()
        else concatenate(n - 1) ++ list

      concatenate(value)
    }
  }

  implicit class RicherInt(richInt: RichInt) {
    def isOdd: Boolean =  richInt.value % 2 != 0
  }

  new RichInt(42).sqrt

  42.isEven // new RichInt(42).isEven
  // type encrichement = pimping

  1 to 10

  import scala.concurrent.duration._
  3.seconds

  // compiler doesn´t do multiple implicit searches.
  //  42.isOdd

  implicit class RichString(string: String) {
    def asInt: Int = Integer.valueOf(string) // java.lang.Integer -> Int
    def encrypt(crypherDistance: Int): String = string.map(c => (c + crypherDistance).asInstanceOf[Char])
  }

  println("3".asInt + 4)
  println("John".encrypt(2))

  3.times(() => println("Scala Rocks!"))
  println(4 * List(1,2))

  // "3" / 4
  implicit def stringToInt(string: String): Int = Integer.valueOf(string)
  println("6" / 2) // stringToInt("6") / 2

  //  equivalent: implicit class RichAlInt(value: Int)
  class RichAlInt(value: Int)
  implicit def enrich(value: Int): RichAlInt = new RichAlInt(value)

  // danger zone
  implicit def intToBoolean(i: Int): Boolean = i == 1

  /*
    if (n) do something
    else do something else
   */

  val aConditionedValue = if (3) "Ok" else "Something wrong"
  println(aConditionedValue)
}
