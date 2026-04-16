package lectures.part4implicits

object Givens extends App {

  val aList = List(2, 4, 3, 1)
  val anOrderedList = aList.sorted //  implicit Ordering[Int]

  //  Scala 2 style
  object Implicits {
    implicit val descendingOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)
  }

  //  Scala 3 style
  object Givens {
    implicit val descendingOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)
    //  givens <=> implicit vals
  }

  //  instantiating an anonymous class
  implicit val descendingOrdering_2: Ordering[Int] = new Ordering[Int] {
      override def compare(x: Int, y: Int) = x - y
  }
}


