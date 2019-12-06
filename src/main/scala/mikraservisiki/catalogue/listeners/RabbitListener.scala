package mikraservisiki.catalogue.listeners

import akka.actor.{ActorSystem, Props}
import com.spingo.op_rabbit.PlayJsonSupport._
import com.spingo.op_rabbit._
import mikraservisiki.catalogue.dto.Items._
import mikraservisiki.catalogue.handler.CatalogueService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.postfixOps

class RabbitListener(catalogueService: CatalogueService)(implicit actorSystem: ActorSystem) {
  private val rabbitControl = actorSystem.actorOf(Props[RabbitControl])

  implicit val recoveryStrategy: RecoveryStrategy = RecoveryStrategy.abandonedQueue()

  def listen(): SubscriptionRef =
    Subscription.run(rabbitControl) {
      import com.spingo.op_rabbit.Directives._

      channel(qos = 3) {
        consume(Queue.passive("order-failed.catalogue-service.queue")) {
          body(as[QueueOrderDto]) { order =>
            val result = catalogueService.freeItemsByOrderId(order.orderId)
            ack(result)
          }
        }
      }
    }
}
