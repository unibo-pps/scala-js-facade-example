package it.unibo.network

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.typedarray.Float32Array
import scala.scalajs.js.JSConverters.*
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Public, pure Scala API for Neural Networks.
 * Hides all Scala.js interop, JavaScript types, and TensorFlow.js memory management.
 */
class Network(inputSize: Int, hiddenSize: Int, outputSize: Int):
  private val model = tf.sequential()

  // Initialize hidden dense layer
  private val hiddenConfig = DenseLayerConfig(units = hiddenSize, activation = "relu", inputShape = js.Array(inputSize))
  model.add(tf.layers.dense(hiddenConfig))

  // Initialize output dense layer
  private val outputConfig = DenseLayerConfig(units = outputSize, activation = "sigmoid")
  model.add(tf.layers.dense(outputConfig))

  // Compile the model with Adam and Binary Crossentropy
  private val compileConfig = ModelCompileConfig(optimizer = tf.train.adam(0.1), loss = "binaryCrossentropy")
  model.compile(compileConfig)

  /**
   * Train the neural network on standard Scala sequences.
   * Cleans up all temporary tensors automatically.
   */
  def train(inputs: Seq[Seq[Double]], outputs: Seq[Seq[Double]], epochs: Int): Future[Unit] =
    // Translate standard Scala nested collections to js.Array
    val jsInputs = js.Array(inputs.map(row => js.Array(row: _*)): _*)
    val jsOutputs = js.Array(outputs.map(row => js.Array(row: _*)): _*)

    val xs = tf.tensor2d(jsInputs)
    val ys = tf.tensor2d(jsOutputs)

    val fitConfig = ModelFitConfig(epochs = epochs, shuffle = true)

    model.fit(xs, ys, fitConfig).toFuture.map { _ =>
      // Crucial: Dispose raw training tensors to avoid memory leaks
      xs.dispose()
      ys.dispose()
      ()
    }

  /**
   * Predict output for a given standard Scala input sequence.
   * Cleans up all temporary prediction tensors automatically.
   */
  def predict(input: Seq[Double]): Seq[Double] =
    val jsInput = js.Array(input: _*)
    val inputTensor = tf.tensor2d(js.Array(jsInput))
    val predictionTensor = model.predict(inputTensor)

    // Synchronously read tensor data and convert Float32Array to standard Scala List
    val data = predictionTensor.dataSync()
    val result = (0 until data.length).map(idx => data(idx).toDouble).toList

    // Crucial: Dispose raw prediction tensors to avoid memory leaks
    inputTensor.dispose()
    predictionTensor.dispose()

    result

// ============================================================================
// Private, internal Scala.js facades targeting TensorFlow.js (hidden from clients)
// ============================================================================

@JSImport("@tensorflow/tfjs", JSImport.Namespace)
@js.native
private[network] object tf extends js.Object:
  def tensor2d(values: js.Array[js.Array[Double]]): Tensor = js.native
  def sequential(): SequentialModel = js.native
  val layers: Layers = js.native
  val train: Train = js.native

@js.native
private[network] trait Tensor extends js.Object:
  def print(): Unit = js.native
  def dispose(): Unit = js.native
  def dataSync(): Float32Array = js.native

@js.native
private[network] trait SequentialModel extends js.Object:
  def add(layer: js.Object): Unit = js.native
  def compile(config: ModelCompileConfig): Unit = js.native
  def fit(inputs: Tensor, outputs: Tensor, config: ModelFitConfig): js.Promise[js.Object] = js.native
  def predict(inputs: Tensor): Tensor = js.native

@js.native
private[network] trait Layers extends js.Object:
  def dense(config: DenseLayerConfig): js.Object = js.native

@js.native
private[network] trait Train extends js.Object:
  def adam(learningRate: Double): Optimizer = js.native

@js.native
private[network] trait Optimizer extends js.Object

// Private strongly-typed configuration traits

private[network] trait DenseLayerConfig extends js.Object:
  var units: Int
  var activation: js.UndefOr[String]
  var inputShape: js.UndefOr[js.Array[Int]]

private[network] object DenseLayerConfig:
  def apply(
    units: Int,
    activation: js.UndefOr[String] = js.undefined,
    inputShape: js.UndefOr[js.Array[Int]] = js.undefined
  ): DenseLayerConfig =
    val result = js.Object().asInstanceOf[DenseLayerConfig]
    result.units = units
    if (activation.isDefined) result.activation = activation
    if (inputShape.isDefined) result.inputShape = inputShape
    result

private[network] trait ModelCompileConfig extends js.Object:
  var optimizer: Optimizer
  var loss: String

private[network] object ModelCompileConfig:
  def apply(optimizer: Optimizer, loss: String): ModelCompileConfig =
    val result = js.Object().asInstanceOf[ModelCompileConfig]
    result.optimizer = optimizer
    result.loss = loss
    result

private[network] trait ModelFitConfig extends js.Object:
  var epochs: Int
  var shuffle: js.UndefOr[Boolean]

private[network] object ModelFitConfig:
  def apply(epochs: Int, shuffle: js.UndefOr[Boolean] = js.undefined): ModelFitConfig =
    val result = js.Object().asInstanceOf[ModelFitConfig]
    result.epochs = epochs
    if (shuffle.isDefined) result.shuffle = shuffle
    result
