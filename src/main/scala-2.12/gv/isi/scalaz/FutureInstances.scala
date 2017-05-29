package gv
package isi.scalaz

import concurrent.{ Future, ExecutionContext }

import scalaz.{ Monad }

trait FutureInstances {

  final class Instances(implicit ec: ExecutionContext) extends Monad[Future] {

    override def point[A](a: ⇒ A): Future[A] = Future successful a

    override def bind[A, B](fa: Future[A])(f: A ⇒ Future[B]): Future[B] = fa flatMap f
  }

  implicit final def futureInstances(implicit ec: ExecutionContext): Instances = new Instances

}
