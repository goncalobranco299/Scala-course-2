package lectures.part4implicits


object ExtensionMethods extends App {

  case class Person(name: String) {
    def greet(): String = s"Hi, I´m $name, how can I help?"
  }

  implicit class RichInt(val value: Int) extends AnyVal {
    def isEven: Boolean = value % 2 == 0

    def sqrt: Double = Math.sqrt(value)

    def times(function: () => Unit): Unit = {
      def timesAux(n: Int): Unit =
        if (n <= 0) ()
        else {
          function()
          timesAux(n - 1)
        }

      timesAux(value)
    }
  }
  val isEven = 3.isEven //  new RichInt(3).isEven


}
