package gv.jleon
package test

object Main {

  def main(args: Array[String]): Unit = {
    import Generators._
    println { arbitrary[Mirror.BaseUrl].sample }
  }

}
