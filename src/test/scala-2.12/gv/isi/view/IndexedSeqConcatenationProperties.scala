package gv
package isi
package view

import org.scalacheck.{ Properties, Prop }

object IndexedSeqConcatenationProperties extends Properties(IndexedSeqConcatenation.getClass.getName) {
  import Prop._
  //  property("gets the right result") = forAll {
  //    (
  //      seq1: Vector[Int],
  //      seq2: Vector[Int],
  //      seq3: Vector[Int],
  //      index: Int
  //    ) ⇒
  //
  //      val seqs: Traversable[IndexedSeq[Int]] = Seq(seq1, seq2, seq3)
  //      val concat: IndexedSeq[Int] = IndexedSeqConcatenation(seqs)
  //
  //      all(
  //        "in seq1" |: (seq1.indices contains index) ==> (concat(index) == seq1(index)),
  //        "in seq2" |: (seq2.indices contains (index - seq1.length)) ==> (concat(index) == seq2(index)),
  //        "in seq3" |: (seq3.indices contains (index - seq1.length - seq2.length)) ==> (concat(index) == seq3(index)),
  //        "not there" |: !(concat.indices contains index) ==> util.Try(concat(index)).isFailure
  //      )
  //  }
}
