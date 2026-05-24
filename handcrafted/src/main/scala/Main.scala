import scala.concurrent.ExecutionContext.Implicits.global
import it.unibo.network.Network

@main def main(): Unit =
  // Define input and output training collections (100% pure standard Scala types)
  val inputs = Seq(
    Seq(0.0, 0.0),
    Seq(0.0, 1.0),
    Seq(1.0, 0.0),
    Seq(1.0, 1.0)
  )
  val outputs = Seq(
    Seq(0.0),
    Seq(1.0),
    Seq(1.0),
    Seq(0.0)
  )

  // Instantiate the high-level, pure Scala Network API (no Scala.js/JS types are exposed!)
  val network = Network(inputSize = 2, hiddenSize = 8, outputSize = 1)

  println("----------------------------------------------------------------")
  println("Training neural network using HAND-CRAFTED TensorFlow.js Facade...")
  println("----------------------------------------------------------------")
  
  // Train the network and handle completion asynchronously via standard Scala Future
  network.train(inputs, outputs, epochs = 1000).foreach { _ =>
    println("Training complete!")
    
    // Perform predictions using pure standard Scala collections
    inputs.foreach { i =>
      val prediction = network.predict(i)
      println(s"In : [${i.mkString(", ")}], out: ${prediction.head}")
    }
  }
