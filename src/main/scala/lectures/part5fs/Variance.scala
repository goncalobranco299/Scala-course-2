package lectures.part5fs

object Variance extends App {

  trait Animal
  class Dog extends Animal
  class Cat extends Animal
  class Crocodile extends Animal

  // what is vairance?
  // "inheritance" - type substitution of generics

  class Cage[T]
  // yes -  covariance
  class CCage[+T]
  val ccage: CCage[Animal] = new CCage[Cat]

  //  no  - invariance
  class ICage[T]
  // val icage: ICage[Animal] = new ICage[Cat]
  // val x: Int = "hello"

  // hello no - opposite = contravariance
  class XCage[-T]
  val XCage: XCage[Cat] = new XCage[Animal]

  class InvariantCage[T](animal: T) // invariant

  // covariant positions
  class CovariantCage[+T](val animal: T)

  // class CovariantVariableCage[+T](var animal: T) // types of vars are in CONTRAVARIANT POSITION

  //  trait AnotherCovariantCage[+T] {
  //  def addAnimal(animal: T) // CONTRAVARIANT POSITION

  class AnotherContravariantCage[-T] {
    def addAnimal(animal: T) = true
  }
  val acc: AnotherContravariantCage[Cat] = new AnotherContravariantCage[Animal]
  acc.addAnimal(new Cat)
  class Kitty extends Cat
  acc.addAnimal(new Kitty)

  class MyList[+A] {
    def add[B >: A](element: B): MyList[B] = new MyList[B] // widening ghe type
  }

  val emptyList = new MyList[Kitty]
  val animals = emptyList.add(new Kitty)
  val moreAnimals = animals.add(new Cat)
  val evenMoreAnimals = moreAnimals.add(new Dog)

  // METHOD ARGUMETNS ARE IN CONTRAVARIANT POSITION

  // return types
  class PetShop[-T] {

    def get[S <: T](isItaPuppy: Boolean, defaultAnimal: S): S = defaultAnimal
  }

  val shop: PetShop[Dog] = new PetShop[Animal]

  class TerraNova extends Dog
  val bigFurry = shop.get(true, new TerraNova)

}
