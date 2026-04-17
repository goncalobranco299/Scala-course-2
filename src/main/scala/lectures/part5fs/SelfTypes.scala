package lectures.part5fs

import lectures.part4implicits.OrganizingImplicits.AlphabeticNameOrdering

import java.awt.Component

object SelfTypes extends App {

  // requiring a type to be mixed in

  trait Instrumentalist {
    def play(): Unit
  }

  trait Singer  { self: Instrumentalist => // whoever implements Singer implement Instrumntalist

    // rest of the implementation or API
    def sing(): Unit
  }

  class LeadSinger extends Singer with Instrumentalist {

    override def play(): Unit = ???
    override def sing(): Unit = ???
  }

  // class Vocalist extends Singer {
  //override def sing(): Unit = ???
  //  }

  val jamesHetfiled = new Singer with Instrumentalist {

    override def sing(): Unit = ???
    override def play(): Unit = ???
  }

  class Guitarist extends Instrumentalist {
    override def play(): Unit = println("(guitar solo)")
  }

  val ericClapton = new Guitarist with Singer {
    override def sing(): Unit = ???
  }

  // vs inheritance
  class A
  class B extends A // B IS AN A

  trait T
  trait S { self: T => } // S REQUIRES a T

  // CAKE PATTERN => "dependency injection"

  class Component {
    // API
  }
  class ComponentA extends Component
  class ComponentB extends Component
  class DependComponent(val component: Component)

  // CAKE PATTERN
  trait ScalaComponent {
    // API
    def action(x: Int): String
  }
  trait ScalaDependComponent { self: ScalaComponent =>
    def dependAction(x: Int): String = action(x) + " this rocks!"
  }
  trait ScalaApplication { slef: ScalaDependComponent => }

  // layer 1 - small components
  trait Picture extends ScalaComponent
  trait Stats extends ScalaComponent

  // layer 2 - compose
  trait Profile extends ScalaDependComponent with Picture
  trait Analytics extends ScalaDependComponent with Stats

  // layer 3 - app
  trait AnalyticsApp extends ScalaApplication with Analytics


  // cyclical dependencies
  class x extends y
  class y extends x

  trait X { self: Y => }
  trait Y { self: X => }



}
