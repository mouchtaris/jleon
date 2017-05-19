package gv.jleon

import scala.reflect.runtime.universe.reify

import test._

object Playground extends AnyRef
    with test.JavaNioGenerator {

  def main(args: Array[String]): Unit = {

    println {
      reify(arbitrary[Path])
    }

  }

}
