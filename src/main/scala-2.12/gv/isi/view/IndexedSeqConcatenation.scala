package gv
package isi
package view

import language.{ postfixOps }
import collection.{ IndexedSeq }

object IndexedSeqConcatenation {

  def Catchall[T]: PartialFunction[Int, T] = PartialFunction.empty

  def apply[T](seqs: Traversable[IndexedSeq[T]]): IndexedSeq[T] = new IndexedSeq[T] {

    private[this] final val getValue: PartialFunction[Int, T] =
      seqs
        .filter(_.nonEmpty) // optimisation
        .foldRight(Catchall[T]) { (seq, fin) ⇒
          seq orElse[Int, T] {
            case i ⇒ fin(i - seq.length)
          }
      }

    final val length: Int = seqs map (_ length) sum

    final def apply(index: Int): T = getValue(index)
  }

}
