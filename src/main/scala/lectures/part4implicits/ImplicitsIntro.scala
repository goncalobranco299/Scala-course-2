package lectures.part4implicits

import jdk.jfr.DataAmount

object ImplicitsIntro extends App {

  val pair = "Daniel" -> "555"
  val intpair = 1 -> 2

  case class Person(name: String) {
    def greet = s"Hi, my name is $name"
  }

  implicit def fromSringToPerson(str: String): Person = Person(str)

  println("Peter".greet) // println(fromStringToPerson("Peter").greet)

  // class A {
  // class A {def greet: Int = 2
  // class A {}
  // class A {implicit def fromStringToA(str: String): A = new A

  // implicit paramaters
    def increment(x: Int)(implicit amount: Int) = x + amount
    implicit val defaultAmount = 10

  increment(2)
  // NOT default args
}
