package it.unibo.network

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

// Library API, used as is
@JSImport("neataptic", "Network")
@js.native
class Network[I, O](val input: Int, val output: Int) extends js.Object:
  def evolve(example: js.Array[Example[I, O]], config: EvolveConfig): js.Promise[_] = js.native
  def activate(elements: js.Array[I]): O = js.native

// If in a certain application I am interest in a subset of the API, I can define only that options
class EvolveConfig(val equal: Boolean, val error: Double, val iterations: Int = 5000) extends js.Object

// js typed object
class Example[I, O](val input: js.Array[I], val output: js.Array[O]) extends js.Object
object Example:
  // enrich API
  def in[I](elems: I*): js.Array[I] = js.Array(elems: _*)
  def out[O](elems: O*): js.Array[O] = js.Array(elems: _*)
  def of[I, O](elements: (js.Array[I], js.Array[O])*): js.Array[Example[I, O]] =
    js.Array(elements.map((in, out) => Example(in, out)): _*)
