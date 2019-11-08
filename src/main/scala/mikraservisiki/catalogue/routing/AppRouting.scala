package mikraservisiki.catalogue.routing

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.{FutureDirectives, MethodDirectives, PathDirectives, RouteDirectives}
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import mikraservisiki.catalogue.handler.CatalogueService

object AppRouting extends RouteDirectives
  with PathDirectives
  with MethodDirectives
  with PlayJsonSupport
  with FutureDirectives {

  import mikraservisiki.catalogue.dto.Items._

  def route(
             catalogueService: CatalogueService
           ): Route =
    pathPrefix("items") {
      (path(LongNumber) & get) { itemId =>
        onSuccess(catalogueService.getItem(itemId)) {
          case Some(item) => complete(item)
          case None => complete(StatusCodes.NotFound)
        }
      } ~ get {
        onSuccess(catalogueService.getItems) { items =>
          complete(items)
        }
      } ~ (path("item") & post & entity(as[ItemCreationParametersDto])) { itemCreationParametersDto =>
        onSuccess(catalogueService.createItem(itemCreationParametersDto)) { item =>
          complete(item)
        }
      } ~ (path(LongNumber / "addition" / LongNumber) & post) { (id, addedAmount) =>
        onSuccess(catalogueService.addExistingItems(id, addedAmount)) { item =>
          complete(item)
        }
        /*
        TODO нужно добавить резервацию заказа:
        эндпоинт типа POST items/{itemid}/order/{orderid}
        в теле запроса передаётся amount
        нужно просто проверить, достаточно ли товаров на складе,
        и в табличку reservations добавить запись с этими параметрами

        в ответ возвращаем ItemDto
         */
      }
    }
}