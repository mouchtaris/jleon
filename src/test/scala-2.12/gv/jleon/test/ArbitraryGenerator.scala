package gv.jleon
package test

trait ArbitraryGenerator {

  final implicit def arbitraryGenerator[T](implicit generator: Gen[T]): Arbitrary[T] =
    Arbitrary(generator)

}
