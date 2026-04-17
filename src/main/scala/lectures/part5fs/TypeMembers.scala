package lectures.part5fs

import lectures.part5fs.TypeMembers.MList

object TypeMembers extends App {

  class Animal
  class Dog extends Animal
  class Cat extends Animal

  class AnimalCollecion {
    type AnimalType // abstract type number
    type BoundedAnimal <: Animal
    type SuperboundedAnimal >: Dog <: Animal
    type AnimalC = Cat
  }

  val ac = new AnimalCollecion
  val dog: ac.AnimalType = ???

  //  val cat: ac.BoundedAnimal = new Cat

  val pup: ac.SuperboundedAnimal = new Dog
  val cat: ac.AnimalC = new Cat

  type CatAlias = Cat
  val anotherCat: CatAlias = new Cat

  // alternative to generics
  trait MyList {
    type T
    def add(element: T): MyList
  }

  class NonEmptyList(value: Int) extends MyList {
    override type T = Int
    def add(element: Int): MyList = ???

  }

  // .type
  type CatsType = cat.type
  val newCat: CatsType = cat
  //  new CatsType

  /*
    Exercise - enforce a type to be applicable to SOME TYPS only
   */
  //  LOCKED
  trait MList {
    type A
    def head: A
    def tail: MList
  }

  trait ApplicableToNumbers {
    type A <: Number
  }
  // NOT OK
  // class CustomList(hd: String, tl: CustomList) extends MList {
  // type A = String
  // def head = hd
  // def tail = tl
  //}

  // OK
  class IntList(hd: Int, tl: IntList) extends MList {
    type A = Int
    def head = hd
    def tail = tl
  }

  // Number
  // type members and type member constraints (bounds)


}

