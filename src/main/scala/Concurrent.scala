import akka.actor.{ActorRef, Props, Actor}

trait MyPromise[A] {
  def complete(value: A)
  def future: MyFuture[A]
  def value: Option[A]
}

trait MyFuture[A] {
  def onComplete(f: A => Unit)
  def value: Option[A]
  def map[B](f: A => B): MyFuture[B]
  def flatMap[B](f: A => MyFuture[B]): MyFuture[B]
  def zip[B](other: MyFuture[B]): MyFuture[(A, B)]
}

class MyPromiseFuture[A] extends MyPromise[A] with MyFuture[A] {
  private[this] var _value = Option.empty[A]
  private[this] var listeners = List.empty[A => Unit]

  def future = this

  def complete(value: A) = {
    if (_value.isDefined)
      throw new IllegalStateException("Promise is already completed!")
    _value = Some(value)
    listeners.foreach(_(value))
    listeners = List.empty
  }

  def value = _value

  def onComplete(f: A => Unit) = value.fold(listeners ::= f)(f)

  def map[B](f: A => B) = {
    val p = new MyPromiseFuture[B]
    onComplete(f andThen p.complete)
    p.future
  }

  def flatMap[B](f: A => MyFuture[B]) = {
    val p = new MyPromiseFuture[B]
    onComplete(a => f(a).onComplete(p.complete))
    p.future
  }

  def zip[B](other: MyFuture[B]) = {
    val p = new MyPromiseFuture[(A, B)]
    onComplete(a => other.value.foreach(b => p.complete(a, b)))
    other.onComplete(b => value.foreach(a => p.complete(a, b)))
    p.future
  }
}


/*
val http: Http
val p = new MyPromiseFuture[Response]
http.doRequest(...).onComplete(p.complete)
p.future
*/