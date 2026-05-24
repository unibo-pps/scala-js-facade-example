# Scala.js Facade Strategies Example

This workspace showcases two distinct methodologies for interacting with JavaScript libraries from **Scala.js 3**, 
using **TensorFlow.js** (`@tensorflow/tfjs` v4.22.0) as the underlying engine.

It has been structured as a multi-project aggregate SBT build, 
splitting the strategies into two  modules:

1. **`handcrafted`**: Manually designed, type-safe facade encapsulated inside a package-private boundary, exposing a 100% pure-Scala API. Trains a Feedforward Neural Network to solve the classic non-linear **XOR gate** problem.
2. **`scalablytyped`**: Automatically generates extensive, ultra-precise Scala.js facades from TypeScript definition files (`.d.ts`) using the **ScalablyTyped** toolchain. 

---

## Prerequisites

To compile and run both subprojects, you need:

- **Java JDK** (recommended: JDK 11, 17, or 21)
- **SBT** (Scala Build Tool)
- **Node.js** (v18+) and **npm**

---

## 1. Handcrafted Module (Neural Network XOR)

This submodule demonstrates the **Private Raw Facade + Pure Scala Wrapper** architectural pattern.
Inside [Network.scala](file:///home/gianluca/Programming/scala-js-facade-example/handcrafted/src/main/scala/it/unibo/network/Network.scala), 
we define package-private (`private[network]`) raw JS facades mapping directly to `@tensorflow/tfjs` imports and classes.
The public `Network` class acts as a clean barrier, 
wrapping all JS promises into Scala `Future`s, translating nested standard Scala sequences into multi-dimensional typed JS arrays, and automatically managing C++ tensor allocations via `.dispose()` to ensure zero memory leaks.

### How to Run:
Launch the handcrafted XOR neural network directly via SBT:

```bash
sbt "handcrafted/run"
```

#### What it does:
1. Builds a Feedforward Neural Network using sequential layers (ReLU hidden layer, Sigmoid output layer).
2. Translates pure Scala datasets (`inputs` and `outputs` lists) into 2D tensors.
3. Trains the model over `1000` epochs using the `adam` optimizer and `binaryCrossentropy` loss.
4. Performs predictions on the standard inputs, showing the trained XOR output (approaching `0` and `1` correctly).
5. Disposes of all input, output, and prediction tensors.

---

## 2. ScalablyTyped Module (Tensor Squaring)

This submodule leverages the **ScalablyTyped** sbt plugin to parse `@tensorflow/tfjs` type definitions and automatically output Scala.js types under the custom package `customtypings`.
It uses a custom [webpack.config.js](file:///home/gianluca/Programming/scala-js-facade-example/scalablytyped/webpack.config.js) specifying target `node`.

### How to Run:
You can run the automatic facade generation, Webpack bundling, and execution in **one single command** without setting any shell environment options (like `NODE_OPTIONS`):

```bash
sbt -J-Xmx4G "scalablytyped/run"
```


#### What it does:
1. Imports and utilizes auto-generated facades from `customtypings.tensorflowTfjs.mod` and associated core math libraries.
2. Creates a 1D tensor representing elements `[1.0, 2.0, 3.0, 4.0]`.
3. Performs a typed squaring operation calling the standalone `square` function from `customtypings.tensorflowTfjsCore.distOpsSquareMod`.
4. Outputs the original and squared tensors via native `.print()` calls inside the Node console.
5. Safely deallocates tensor memory via `.dispose()`.
