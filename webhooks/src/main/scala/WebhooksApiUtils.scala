package webhooks

import cats.effect.IO
import requests.Response

object WebhooksApiUtils {

  private def headers(token: String): Map[String, String] = Map(
    "Authorization" -> s"Bearer $token",
    "Accept" -> "application/vnd.github+json",
  )

  private def listWebhooks(owner: String, repository: String, token: String): IO[Response] = {
    val apiUrl = s"https://api.github.com/repos/$owner/$repository/hooks"

    IO(requests.get(apiUrl, headers = headers(token)))
  }

  def listMatchingWebhooks(
    owner: String,
    repository: String,
    token: String,
    webhookUrl: String,
    events: Option[Set[String]],
  ): IO[Set[String]] = {
    for {
      response <- listWebhooks(owner, repository, token)
      webhooks <- IO(ujson.read(response.text()).arr)
      matchingWebhooks = webhooks.filter { webhook =>
        val webhookUrlMatches = webhook("config")("url").str == webhookUrl
        val webhookEventsMatches = events.isEmpty || events.contains(webhook("events").arr.map(_.str).toSet)
        webhookUrlMatches && webhookEventsMatches
      }
    } yield matchingWebhooks.map(_.obj("id").str).toSet
  }

  def createWebhook(
    owner: String,
    repository: String,
    webhookUrl: String,
    token: String,
    events: Set[String],
  ): IO[String] = {
    val apiUrl = s"https://api.github.com/repos/$owner/$repository/hooks"
    val payload = ujson.Obj(
      "name" -> "web",
      "active" -> true,
      "events" -> ujson.Arr(events),
      "config" -> ujson.Obj(
        "url" -> webhookUrl,
        "content_type" -> "json",
        "insecure_ssl" -> "0",
      ),
    )

    for {
      response <- IO(requests.post(apiUrl, data = payload, headers = headers(token)))
      hookId <- IO(ujson.read(response.text())("id").str)
    } yield hookId
  }

  def enableWebhook(owner: String, repository: String, token: String, hookId: String): IO[Unit] =
    setWebhookActive(owner, repository, token, hookId, active = true)

  def disableWebhook(owner: String, repository: String, token: String, hookId: String): IO[Unit] =
    setWebhookActive(owner, repository, token, hookId, active = false)

  def setWebhookActive(owner: String, repository: String, token: String, hookId: String, active: Boolean): IO[Unit] = {
    val apiUrl = s"https://api.github.com/repos/$owner/$repository/hooks/$hookId"
    val payload = ujson.Obj(
      "active" -> active
    )

    for {
      response <- IO(requests.patch(apiUrl, data = payload, headers = headers(token)))
      _ <- IO.raiseUnless(response.statusCode == 200)(
        new RuntimeException(s"Error disabling webhook: ${response.statusMessage}")
      )
    } yield ()
  }

  def createWebhookIfMissing(
    owner: String,
    repository: String,
    webhookUrl: String,
    token: String,
    events: Set[String],
  ): IO[String] = {
    for {
      matchingWebhooks <- listMatchingWebhooks(owner, repository, token, webhookUrl, Some(events))

      hookId <-
        if (matchingWebhooks.nonEmpty) {
          IO.println("Webhook already exists") *> IO.pure(matchingWebhooks.head)
        } else {
          IO.println("Creating webhook") *>
            createWebhook(owner, repository, webhookUrl, token, events)
        }
    } yield hookId
  }
}
