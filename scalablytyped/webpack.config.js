// Monkeypatch crypto.createHash to map md4 to sha256 to avoid OpenSSL 3.0 errors without NODE_OPTIONS
const crypto = require("crypto");
const originalCreateHash = crypto.createHash;
crypto.createHash = (algorithm, options) => {
  return originalCreateHash(algorithm === "md4" ? "sha256" : algorithm, options);
};

const config = require("./scalajs.webpack.config");
config.target = "node";
module.exports = config;
