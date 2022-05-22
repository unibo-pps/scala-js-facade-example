import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

import it.unibo.network.*

@main def main(): Unit =
  import Example.*
  // Library usage
  val trainingSet = Example.of(
    in(0, 0) -> out(0),
    in(0, 1) -> out(1),
    in(1, 0) -> out(1),
    in(1, 1) -> out(0)
  )
  val network = Network[Int, Int](2, 1)
  // network.activate(in("ciao")) // error
  val config = EvolveConfig(true, 0.0, 1000)
  network.evolve(trainingSet, config).`then` { _ =>
    in(0, 0) :: in(1, 0) :: in(0, 1) :: in(1, 1) :: Nil foreach (i => println(s"In : $i, out: ${network.activate(i)}"))
  }
