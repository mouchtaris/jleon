package gv.jleon

import org.scalacheck.{
  Prop ⇒ scProp,
  Properties ⇒ scProperties,
  Arbitrary ⇒ scArbitrary,
  Gen ⇒ scGen
}

package object test extends AnyRef //  with ArbitraryGenerator
{

  type Prop = scProp
  val Prop = scProp

  type Properties = scProperties

  type Gen[T] = scGen[T]
  val Gen = scGen
  type Arbitrary[T] = scArbitrary[T]
  val Arbitrary = scArbitrary
  def arbitrary[T: Arbitrary]: Gen[T] = Arbitrary.arbitrary[T]

  def genOne[T: Arbitrary]: Option[T] =
    arbitrary[T].apply(Gen.Parameters.default, org.scalacheck.rng.Seed.random())

  def genStream[T: Arbitrary]: Stream[Option[T]] =
    genOne[T] #:: genStream[T]
}
