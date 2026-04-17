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

  /*
    Big rule
    - method arguments are in CONTRAVARIANT postition
    - return types are in COVARIANT position
   */

  /**
   * 1. Invariant, covariant, contravariant
   *  Parking[T](things:  List[T]
   *  ipound(vechiles:  List[T])
   *  checkVechiles(conditios: String): List[T]
   *
   *  2. used somoene else's API ILIst[T]
   *
   */

  class Vechile
  class Bike extends Vechile
  class Car extends Vechile
  class IList[T]

  class IParking[T](vechile: List[T]) {
    def park(vechile: T): IParking[T] = ???
    def impound(vechiles: List[T]): IParking[T] = ???
    def checkVehicls(conditions: String): List[T] = ???

    def flatMap[S](f: T => IParking[S]): IParking[S] = ???
  }

  class CParking[+T](vechile: List[T]) {
    def park[S >: T](vechile: S): CParking[S] = ???
    def impount[S >: T](vechile: List[S]): CParking[S] = ???
    def checkVehicles(conditions: String): List[T] = ???

    def flatMap[S](f: T => IParking[S]): IParking[S] = ???

  }

  class XParking[-T](vechile: List[T]) {
    def park(vechile: T): XParking[T] = ???
    def impount(vechile: List[T]): XParking[T] = ???
    def checkVehicles[S <: T](conditions: String): List[S] = ???

    def flatMap[R <: T, S](f: Function[R, XParking[S]]): XParking[S] = ???

  }

  /*
    Rule of thumb
    - use covariance = COLLECION OF THINGS
    - use contravariance = GROUP OF ACTIONS
   */

  class CParking2[+T](vechiles: IList[T]) {
    def park[S >: T](vechiles: S): CParking2[S] = ???
    def impount[S >: T](vechiles: List[S]): CParking2[S] = ???
    def checkVehicles(conditions: String): List[T] = ???
  }

  class XParking2[-T](vechiles: List[T]) {
    def park(vechiles: T): XParking2[T] = ???
    def impount[S <: T](vechiles: List[S]): XParking2[S] = ???
    def checkVehicles[S <: T](conditions: String): IList[S] = ???
  }

  /*
  Rule
   */

}
