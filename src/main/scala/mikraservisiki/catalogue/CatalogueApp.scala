package mikraservisiki.catalogue

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import mikraservisiki.catalogue.dao.RelationalItemsDao
import mikraservisiki.catalogue.handler.CatalogueServiceImpl
import mikraservisiki.catalogue.listeners.RabbitListener
import mikraservisiki.catalogue.routing.AppRouting

import scala.concurrent.ExecutionContextExecutor
import scala.sys.addShutdownHook

object CatalogueApp extends App {
  implicit val system: ActorSystem = ActorSystem()
  implicit val executor: ExecutionContextExecutor = system.dispatcher
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  val config = ConfigFactory.load()
  val logger = Logging(system, getClass)
  val catalogueService = new CatalogueServiceImpl(RelationalItemsDao)
  val routes = AppRouting.route(catalogueService)
  Http().bindAndHandle(
    routes, config.getString("http.interface"), config.getInt("http.port")
  )
  new RabbitListener(catalogueService).listen()
  addShutdownHook(system.terminate())
}