# WORKSPACE
load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

http_archive(
    name = "bazel_skylib",
    sha256 = "b8a1527901774180afc798aeb28c4634bdccf19c4d98e7bdd1ce79d1fe9aaad7",
    urls = [
        "https://mirror.bazel.build/github.com/bazelbuild/bazel-skylib/releases/download/1.4.1/bazel-skylib-1.4.1.tar.gz",
        "https://github.com/bazelbuild/bazel-skylib/releases/download/1.4.1/bazel-skylib-1.4.1.tar.gz",
    ],
)

load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

http_archive(
    name = "io_bazel_rules_scala",
    sha256 = "9a23058a36183a556a9ba7229b4f204d3e68c8c6eb7b28260521016b38ef4e00",
    strip_prefix = "rules_scala-6.4.0",
    url = "https://github.com/bazelbuild/rules_scala/releases/download/v6.4.0/rules_scala-v6.4.0.tar.gz",
)

load("@io_bazel_rules_scala//:scala_config.bzl", "scala_config")

# Stores Scala version and other configuration
# 2.12 is a default version, other versions can be use by passing them explicitly:
# scala_config(scala_version = "2.11.12")
# Scala 3 requires extras...
#   3.2 should be supported on master. Please note that Scala artifacts for version (3.2.2) are not defined in
#   Rules Scala, they need to be provided by your WORKSPACE. You can use external loader like
#   https://github.com/bazelbuild/rules_jvm_external
scala_config(scala_version = "2.13.12")

load("@io_bazel_rules_scala//scala:scala.bzl", "rules_scala_setup", "rules_scala_toolchain_deps_repositories")

# loads other rules Rules Scala depends on
rules_scala_setup()

# Loads Maven deps like Scala compiler and standard libs. On production projects you should consider
# defining a custom deps toolchains to use your project libs instead
rules_scala_toolchain_deps_repositories(fetch_sources = True)

load("@rules_proto//proto:repositories.bzl", "rules_proto_dependencies", "rules_proto_toolchains")

rules_proto_dependencies()

rules_proto_toolchains()

load("@io_bazel_rules_scala//scala:toolchains.bzl", "scala_register_toolchains")

scala_register_toolchains()

# optional: setup ScalaTest toolchain and dependencies
load("@io_bazel_rules_scala//testing:scalatest.bzl", "scalatest_repositories", "scalatest_toolchain")

scalatest_repositories()

scalatest_toolchain()

RULES_JVM_EXTERNAL_TAG = "4.3"

RULES_JVM_EXTERNAL_SHA = "6274687f6fc5783b589f56a2f1ed60de3ce1f99bc4e8f9edef3de43bdf7c6e74"

http_archive(
    name = "rules_jvm_external",
    sha256 = RULES_JVM_EXTERNAL_SHA,
    strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
    url = "https://github.com/bazelbuild/rules_jvm_external/archive/%s.zip" % RULES_JVM_EXTERNAL_TAG,
)

load("@rules_jvm_external//:defs.bzl", "maven_install")

maven_install(
    artifacts = [
        "com.softwaremill.sttp.client3:core_2.13:3.3.15",
        "com.softwaremill.sttp.client3:circe_2.13:3.3.15",
        "io.circe:circe-generic_2.13:0.14.1",
        "com.lihaoyi:requests_2.13:0.8.0",
        "com.lihaoyi:ujson_2.13:3.1.3",
        "com.lihaoyi:upickle-core_2.13:3.1.3",
        "com.lihaoyi:geny_2.13:1.0.0",
        "org.typelevel:cats-core_2.13:2.6.1",
        "org.typelevel:cats-effect_2.13:3.2.9",
        "com.typesafe.akka:akka-http_2.13:10.2.7",
        "com.typesafe.akka:akka-stream_2.13:2.6.18",
        "com.typesafe.akka:akka-http-spray-json_2.13:10.2.7",
    ],
    repositories = [
        "https://repo1.maven.org/maven2",
    ],
)
