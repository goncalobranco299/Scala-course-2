package lectures.part5fs

import lectures.part5fs.Variance.Crocodile

object FBoundedpolymorphism extends App {


  /*trait Animal {
    def breed: List[Animal]
  }

  class Cat extends Animal {
    override def breed: List[Animal] = ??? // List[Cat] !!
  }

  class Dog extends Animal {
    override def breed: List[Animal] = ??? // List[Dog] !!
  }*/

    //  Solution 1 - naive

  /*trait Animal {
    def breed: List[Animal]
  }

  class Cat extends Animal {
    override def breed: List[Cat] = ??? // List[Cat] !!
  }

  class Dog extends Animal {
    override def breed: List[Cat] = ??? // List[Dog] !!
  }*/

  /*trait Animal[A <: Animal[A]] {  // recursive type: F-Bounded Polymorphism
    def breed: List[Animal[A]]
  }

  class Cat extends Animal[Cat]{
    override def breed: List[Animal[Cat]] = ??? // List[Cat] !!
  }

  class Dog extends Animal [Dog] {
    override def breed: List[Animal[Dog]] = ??? // List[Dog] !!
  }*/

  /*trait Entity[E <: Entity[E]] // ORM
  class Person extends Comparable[Person] {
    override def compareTo(o: Person): Int = ???
  }

  class Crocodile extends Animal[Dog] {
    override def breed: List[Animal[Dog]] = ??? // List[Dog] !!
  }*/

  // Solution 3 - FBP + self-types

  /*trait Animal[A <: Animal[A]] {  self: A =>
   def breed: List[Animal[A]]
  }

  class Cat extends Animal[Cat]{
   override def breed: List[Animal[Cat]] = ??? // List[Cat] !!
  }

  class Dog extends Animal [Dog] {
   override def breed: List[Animal[Dog]] = ??? // List[Dog] !!
  }

  /*class Crocodile extends Animal[Crocodile] {
    override def breed: List[Animal[Crocodile]] = ??? // List[Dog] !!
  }*/

  trait Fish extends Animal[Fish]
  class Shark extends Fish {
    override def breed: List[Animal[Fish]] = List(new Cod) // wrong
  }

  class Cod extends Fish {
    override def breed: List[Animal[Fish]] = ???
  }
*/
  // Exercise

  // Solution 4 type classes!

  /*trait Animal
  trait CanBread[A] {
    def breed(a: A): List[A]
  }

  class Dog extends Animal
  object Dog {
    implicit object DogsCanBreed extends CanBread[Dog] {
      def breed(a: Dog): List[Dog] = List()
    }
  }

  implicit class CanBreedOps[A](animal: A) {
    def breed(implicit canBreed: CanBread[A]): List[A] =
      canBreed.breed(animal)
  }

  val dog = new Dog
  dog.breed
  /*
    new CanBreedOps[Dog](dog).breed
    implicit value to pass to bread: Dog.DogsCanBreed
   */

  class Cat extends Animal
  object Cat {
    implicit object CatsCanBreed extends CanBread[Dog] {
      def breed(a: Dog): List[Dog] = List()
    }
  }

  val cat = new Cat
  cat.breed*/
  trait Animal[A] {
    def breed(a: A): List[A]
  }

  class Dog
  object Dog {
    implicit object DogAnimal extends Animal[Dog] {
      override def breed(a: Dog): List[Dog] = List()
    }
  }

  class Cat
  object Cat {
    implicit object DogAnimal extends Animal[Cat] {
      override def breed(a: Cat): List[Cat] = List()
    }
  }

  implicit class AnimalOps[A](animal: A) {
    def breed(implicit animalTypeClassInstance: Animal[A]): List[A] =
      animalTypeClassInstance.breed(animal)
  }
  val dog = new Dog
  dog.breed

  val cat = new Cat
  cat.breed

}
