package mikraservisiki.catalogue.handler

import mikraservisiki.catalogue.dao.ItemsDao
import mikraservisiki.catalogue.dto.Items.{ItemCreationParametersDto, ItemDto}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


trait CatalogueService {
  def getItems: Future[Seq[ItemDto]]

  def getItem(id: Long): Future[Option[ItemDto]]

  def createItem(item: ItemCreationParametersDto): Future[ItemDto]

  def addExistingItems(id: Long, addedAmount: Long): Future[Option[ItemDto]]

}

class CatalogueServiceImpl(itemsDao: ItemsDao) extends CatalogueService {
  override def getItems: Future[Seq[ItemDto]] =
    itemsDao.getItems.flatMap(Future.traverse(_) { item =>
      itemsDao.getReservedAmount(item.id).map { reservationAmount =>
        ItemDto(item.id, item.name, item.price, item.initialAmount - reservationAmount)
      }
    })

  override def getItem(id: Long): Future[Option[ItemDto]] = {
    val itemOptFuture = itemsDao.getItem(id)
    val reservationsFuture = itemsDao.getReservedAmount(id)

    for (
      itemOpt <- itemOptFuture;
      reservationAmount <- reservationsFuture
    ) yield {
      itemOpt.map(item => ItemDto(item.id, item.name, item.price, item.initialAmount - reservationAmount))
    }
  }

  override def createItem(item: ItemCreationParametersDto): Future[ItemDto] =
    itemsDao.createItem(item.name, item.price, item.amount).flatMap { item =>
      Future(ItemDto(item.id, item.name, item.price, item.initialAmount))
    }

  override def addExistingItems(id: Long, addedAmount: Long): Future[Option[ItemDto]] =
    itemsDao.addExistingItems(id, addedAmount).flatMap {
      case true => getItem(id)
      case false => Future.successful(None)
    }

}
