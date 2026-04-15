package lectures.part3async

import lectures.part1as.AdvancedPatternMatching.Person

import javax.naming.spi.DirStateFactory.Result
import scala.concurrent.{Await, Future, Promise}
import scala.util.{Failure, Random, Success, Try}
import scala.concurrent.duration._

// important fot futures
import scala.concurrent.ExecutionContext.Implicits.global

object FuruturesPromises extends App {
  def calculateMeaningOfLife: Int = {
    Thread.sleep(2000)
    42
  }

  val aFuture = Future {
    calculateMeaningOfLife // calculates the meaning of life on ANOTHER thread
  } // (global) wich is passed by the compiler

  println(aFuture.value) // Option[Try[Int]]

  println("Waiting on the future")
  aFuture.onComplete {
    case Success(meaningOfLife) => println(s"the meaning of life is $meaningOfLife")
    case Failure(e) => println(s"I have failed with $e")
  } // SOME thread

  Thread.sleep(3000)

  // mini social network

  case class Profile(id: String, name: String) {
    def poke(anotherProfile: Profile): Unit =
      println(s"${this.name} poking ${anotherProfile.name}")
  }
  object SocialNetwork {
    // "database"
    val names = Map(
      "fb.id.1-zuck" -> "Mark",
      "fb-id.2-bill" -> "Biil",
      "fb-id.3-dummy" -> "Dummy"
    )
    val friends = Map(
      "fb.id.1-zuck" -> "fb-id.2-bill"
    )

    val random = new Random()

    // API
    def fetchProfile(id: String): Future[Profile] = Future {
      //  fetching from the DB
      Thread.sleep(random.nextInt(300))
      Profile(id,names(id))
    }
    def fetchBestFriend(profile: Profile): Future[Profile] = Future {
      Thread.sleep(random.nextInt(400))
      val bfId = friends(profile.id)
      Profile(bfId,names(bfId))
    }
  }
  // client: mark to poke bill
  val mark = SocialNetwork.fetchProfile(id = "fb.id.1-zuck")
  // mark.onComplete {
  //case Success(markProfile) => {
  //val bill = SocialNetwork.fetchBestFriend(markProfile)
  //bill.onComplete{
  //case Success(billProfile) => markProfile.poke(billProfile)
  //case Failure(e) => e.printStackTrace()
  //}
  //}
  //case Failure(ex) => ex.printStackTrace()
  //}

  // functional composition of futures
  // map, flatMap,  filter
  val nameOnTheWall = mark.map(profile => profile.name)

  val marksBestFriend = mark.flatMap(profile => SocialNetwork.fetchBestFriend(profile))

  val zucksBestFriendRestriced = marksBestFriend.filter(profile => profile.name.startsWith("Z"))

  // for-comprehensions
  for {
    mark <- SocialNetwork.fetchProfile(id = "fb.id.1-zuck")
    bill <- SocialNetwork.fetchBestFriend(mark)
  } mark.poke(bill)

  Thread.sleep(1000)

  // fallbakcs
  val aProfileNoMatterWhat = SocialNetwork.fetchProfile(id = "unknown id").recover {
    case e: Throwable => Profile("fb.id.0-dummy", "Forever alone")
  }

  val aFetchedProfileNoMatterWhat = SocialNetwork.fetchProfile(id = "unknown id").recoverWith {
    case e: Throwable => SocialNetwork.fetchProfile(id = "fb.id.0-dummy")
  }

  val fallbackReuslt = SocialNetwork.fetchProfile(id = "unknow id").fallbackTo(SocialNetwork.fetchProfile(id = "fb.id.0-dummy"))

  // online banking app
  case class User(name: String)
  case class Transaction(sender: String, receiver: String, amount: Double, status: String)

  object BankingApp {
    val name = "Rock the JVM banking"

    def fetchUser(name: String): Future[User] = Future {
      //simulate fetching from the DB
      Thread.sleep(500)
      User(name)
    }
    def createTransacion(user: User,merchantName: String, amount: Double): Future[Transaction] =  Future {
      // simulate some processes
      Thread.sleep(1000)
      Transaction(user.name, merchantName, amount, "Success")
    }
    def purchase(username: String, item: String, merchantName: String, cost: Double): String = {
      // fetch the user from the DB
      // create a transaction
      // WAIT for the transaction to finish
      val transactionStatusFuture = for {
        user <- fetchUser(username)
        transaction <- createTransacion(user, merchantName, cost)
      } yield transaction.status

      Await.result(transactionStatusFuture,2.seconds) // implicit conversions -> pimp my libary
    }
  }
  println(BankingApp.purchase("Daniel", "iPhone 17 pro max", "rock the jvm store", 3000))

  // promises

  val promise = Promise[Int]() // "controler" over a future
  val future = promise.future

  // thread 1- "consumer"
  future.onComplete {
    case Success(r) => println("[consumer] I´ve received " + r)
  }

  // thread 2 - "producer"
  val producer = new Thread(() => {
    println("[producer] crunching numbers...")
    Thread.sleep(500)
    // "fulfilling" the promise
    promise.success(42)
    println("[producer] done")
  })
  producer.start()
  Thread.sleep(1000)

  /*
    1) filfill a future IMMEDIATELY with a value
    2) inSequence(fa, fb)
    3) first(fa, fb) => new future with the first of the two futures
    4) last(fa,fb) => new future with the last value
    5) retryUntil(action: () => Future[T], condition: T => Boolean): Future[T]
   */

  // 1 - fulfill immediately
  def fulfillImmediately[T](value: T): Future[T] = Future(value)
  // 2 - insequence
  def inSequence[A, B](first: Future[A],second: Future[B]): Future[B] =
    first.flatMap(_ => second)

  //  3 - first out of two futures
  def first[A](fa: Future[A], fb: Future[A]): Future[A] = {
    val promise = Promise[A]
    fa.onComplete(promise.tryComplete)
    fa.onComplete(promise.tryComplete)


    promise.future
  }

  // 4 - last out of the two futures
  def last[A](fa: Future[A], fb: Future[A]): Future[A] = {
    // 1 promise wich both futures will try to complete
    // 2 promise wich the LAST future will complete
    val bothPromise = Promise[A]
    val lastPromise = Promise[A]
    val checkAndComplete = (result:  Try[A]) =>
      if(!bothPromise.tryComplete(result))
        lastPromise.complete(result)

    fa.onComplete(checkAndComplete)
    fa.onComplete(checkAndComplete)

    lastPromise.future
  }

  val fast = Future {
    Thread.sleep(100)
    42
  }

  val slow = Future {
    Thread.sleep(200)
    45
  }
  first(fast, slow).foreach(f => println("FIRST: " + f))
  last(fast, slow).foreach(l => println("LAST " + l))

  Thread.sleep(1000)

  // retry until
  def retryUnitl[A](action: () => Future[A],  condition: A => Boolean): Future[A] =
    action()
      .filter(condition)
      .recoverWith {
        case _ => retryUnitl(action, condition)
      }

  val random = new Random()
  val action = () => Future {
    Thread.sleep(100)
    val nextValue = random.nextInt(100)
    println("generated " + nextValue)
    nextValue
  }

  retryUnitl(action, (x: Int) => x < 10).foreach(result => println("settled at " + result))
  Thread.sleep(1000)
}

