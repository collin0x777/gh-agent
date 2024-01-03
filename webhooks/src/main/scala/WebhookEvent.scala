package webhooks

sealed trait WebhookEvent

sealed trait WebhookEventCompanion {
  val eventName: String
  def parse(json: String): Option[WebhookEvent]
}

object WebhookEvent {
  private val eventCompanions: Set[WebhookEventCompanion] = Set(
    PullRequestReviewComment
  )

  def parse(event: String, json: String): Option[WebhookEvent] =
    eventCompanions.find(_.eventName == event).flatMap(_.parse(json))

  // todo should become a sealed trait for created/edited/deleted etc
  case class Issues(
    action: String,
    body: String,
    title: String,
  ) extends WebhookEvent

  private object Issues extends WebhookEventCompanion {
    override val eventName: String = "issues"
    override def parse(json: String): Option[WebhookEvent] = ???
  }

  // todo should become a sealed trait for created/edited/deleted etc
  case class PullRequestReviewComment(
    action: String
    //  comment: Comment,
    //  pull_request: PullRequest,
    //  repository: Repository,
    //  sender: Sender,
  ) extends WebhookEvent

  private object PullRequestReviewComment extends WebhookEventCompanion {
    override val eventName: String = "pull_request_review_comment"
    override def parse(json: String): Option[WebhookEvent] = ???
  }
}
