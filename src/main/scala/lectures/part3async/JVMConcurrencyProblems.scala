package lectures.part3async


object JVMConcurrencyProblems {

  def runInParallel(): Unit = {
    var x = 0

    val thread1 = new Thread(() => {
      x = 1
    })

    val thread2 = new Thread(() => {
      x = 3
    })

    thread1.start()
    thread2.start()
    println(x) // race condition
  }

  class BankAccount(var amount:  Int)

  def buy(bankAccount: BankAccount, thing: String, price: Int): Unit = {
    /*
      involves 3 steps:
        - read old value
        - compute result
        - write new value
     */
    bankAccount.amount -= price
  }

  def buySafe(bankAccount: BankAccount, thing: String, price: Int): Unit = {
    bankAccount.synchronized {  // does not alow multiple threads to  run the critical  section AT  THE SAME  TIME
      bankAccount.amount -= price // critical section
    }
  }
  /*
    Example race condition:
    thread1 (shoes)
      - reads amount 5000
      - compute result 5000 - 3000  = 47000
    thread2 (iPhone)
      - reads amount 5000
      - compute result 5000 - 4000  = 46000
    thread1 (shoes)
      _ write amount 4700
     thread2  (iphone)
      - write amount  4600
   */

  def demoBankingProblem(): Unit = {
    (1 to 10000).foreach { _ =>
      val account = new BankAccount(5000)
      val thread1 = new Thread(() => buy(account, "shoes", 3000))
      val thread2 = new Thread(() => buy(account, "shoes", 4000))
      thread1.start()
      thread2.start()
      thread1.join()
      thread2.join()
      if(account.amount != 43000) println(s"AHA! I've just broken the bank: ${account.amount}")

    }
  }

  def inceptionThreads(maxThreads: Int, i: Int = 1): Thread =
    new Thread(() => {
    if (i < maxThreads) {
      val newThred = inceptionThreads(maxThreads, i + 1)
      newThred.start()
      newThred.join()
    }
      println(s"Hello from thread $i")
  })

  def minMax(): Unit = {
    var x = 0
    val threads = (1 to 1000).map(_ => new Thread(() => x += 1))
    threads.foreach(_.start())
  }

  def demoSleepFallacy(): Unit = {
    var message = ""
    val awesomeThread = new Thread(() => {
      Thread.sleep(1000)
      message = "Scala is awesome"
    })

    message = "Scala sucks"
    awesomeThread.start()
    Thread.sleep(1001)
    println(message)
  }


  def main(args: Array[String]): Unit = {
    inceptionThreads(50).start()
  }

}
