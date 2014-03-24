import akka.actor.{ActorSystem, Props, ActorRef, Actor}

object Counter {
  case class Inc(by: Int)
  case object Current
  case class CurrentReply(count: Int)
}

class Counter(private[this] var count: Int) extends Actor {
  def receive = {
    case Counter.Inc(by) => count += by
    case Counter.Current => sender ! Counter.CurrentReply(count)
  }
}

class Increaser(manager: ActorRef, counter: ActorRef) extends Actor {
  (1 to 100).foreach { _ => counter ! Counter.Inc(1) }
  counter ! Counter.Current

  def receive = { case msg => manager ! msg }
}

class Manager extends Actor {
  println("Starting Manager")
  val counter = context.actorOf(Props(classOf[Counter], 0), "counter")

  val children = (1 to 10).map { i => context.actorOf(
    Props(classOf[Increaser], self, counter), s"increaser-$i"
  ) }

  def receive = {
    case Counter.CurrentReply(count) =>
      println(s"$sender reported count: $count")
  }
}

object Main {
  def main(args: Array[String]) {
    println("Creating actor system")
    val system = ActorSystem("main")
    println("Starting manager")
    system.actorOf(Props(classOf[Manager]), "manager")
    println("Waiting for keypress")
    Console.readLine()
  }
}