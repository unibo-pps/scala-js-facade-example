import scala.scalajs.js
import customtypings.tensorflowTfjs.mod as tf
import customtypings.tensorflowTfjsCore.distTypesMod.*
import customtypings.tensorflowTfjsCore.distOpsSquareMod.*
@main def main(): Unit =
  println("================================================================")
  println("Demonstrating ScalablyTyped automatic facade generation...")
  println("================================================================")

  // Create a 1D tensor using the automatically generated facade package (typings.tensorflowTfjs.mod)
  val data = js.Array(1.0, 2.0, 3.0, 4.0)
  val tensor = tf.tensor1d(data.asInstanceOf[TensorLike1D])

  println("Original tensor values (printed via JS engine):")
  tensor.print()

  // Perform a squaring operation
  val squared = square(tensor)
  println("Squared tensor values (printed via JS engine):")
  squared.print()

  // Clean up WebGL/WASM memory
  tensor.dispose()
  squared.dispose()

  println("Done!")
