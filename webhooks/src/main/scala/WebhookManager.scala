package webhooks

import cats.effect.unsafe.implicits.global

import java.util.UUID

case class WebhookManager private(
  owner: String,
  repository: String,
  token: String,
  webhookUrl: String,
  hookId: String,
) {
  def enable(): Unit = {
    WebhooksApiUtils.enableWebhook(owner, repository, token, hookId).unsafeRunSync()
  }

  def disable(): Unit = {
    WebhooksApiUtils.disableWebhook(owner, repository, token, hookId).unsafeRunSync()
  }
}

object WebhookManager {
  private val gh_agent_url = "https://api.collin0x777.com/gh-agent"
  private val events = Set("push")

  def start(owner: String, repository: String, token: String): WebhookManager = {
    val agentId = UUID.randomUUID().toString
    val webhookUrl = s"$gh_agent_url/$agentId"
    val hookId = WebhooksApiUtils.createWebhook(owner, repository, webhookUrl, token, events).unsafeRunSync()

    WebhookManager(owner, repository, token, webhookUrl, hookId)
  }
}
