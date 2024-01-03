load("@io_bazel_rules_scala//scala:scala.bzl", "scala_binary", "scala_library", "scala_test")

scala_binary(
    name = "webhooks_bin",
    srcs = [
        "src/main/scala/Main.scala",
    ],
    main_class = "webhooks.Main",
    deps = [
        ":webhooks_lib",
    ],
)

scala_library(
    name = "webhooks_lib",
    srcs = [
        "src/main/scala/WebhookEndpoint.scala",
        "src/main/scala/WebhookManager.scala",
        "src/main/scala/WebhooksApiUtils.scala",
    ],
    exports = [
        "@maven//:com_lihaoyi_geny_2_13",
        "@maven//:com_lihaoyi_requests_2_13",
        "@maven//:com_lihaoyi_ujson_2_13",
        "@maven//:com_lihaoyi_upickle_core_2_13",
        "@maven//:com_typesafe_akka_akka_actor_2_13",
        "@maven//:com_typesafe_akka_akka_http_2_13",
        "@maven//:com_typesafe_akka_akka_http_core_2_13",
        "@maven//:com_typesafe_akka_akka_http_spray_json_2_13",
        "@maven//:com_typesafe_akka_akka_stream_2_13",
        "@maven//:io_circe_circe_generic_2_13",
        "@maven//:org_typelevel_cats_core_2_13",
        "@maven//:org_typelevel_cats_effect_2_13",
    ],
    deps = [
        "@maven//:com_lihaoyi_geny_2_13",
        "@maven//:com_lihaoyi_requests_2_13",
        "@maven//:com_lihaoyi_ujson_2_13",
        "@maven//:com_lihaoyi_upickle_core_2_13",
        "@maven//:com_typesafe_akka_akka_actor_2_13",
        "@maven//:com_typesafe_akka_akka_http_2_13",
        "@maven//:com_typesafe_akka_akka_http_core_2_13",
        "@maven//:com_typesafe_akka_akka_http_spray_json_2_13",
        "@maven//:com_typesafe_akka_akka_stream_2_13",
        "@maven//:io_circe_circe_generic_2_13",
        "@maven//:org_typelevel_cats_core_2_13",
        "@maven//:org_typelevel_cats_effect_2_13",
    ],
)
