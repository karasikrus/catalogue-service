package mikraservisiki.catalogue.handler

import mikraservisiki.catalogue.dto.Items.{ItemAdditionParametersDto, ItemCreationParametersDto, ItemDto}
import mikraservisiki.catalogue.dao.ItemsDao
import mikraservisiki.catalogue.schema.TableDefinitions.Item

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


trait CatalogueService{
  def getItems: Future[Seq[ItemDto]]

  def getItem(id: Long): Future[Option[ItemDto]]

  def createItem(item: ItemCreationParametersDto): Future[ItemDto]

  def addExistingItems(id: Long, addedAmount: Long): Future[Option[ItemDto]]

  def subtractExistingItems(id: Long, subtractedAmount: Long): Future[Option[ItemDto]]

}

class CatalogueServiceImpl(itemsDao: ItemsDao) extends CatalogueService {
  override def getItems: Future[Seq[ItemDto]] = itemsDao.getItems.flatMap{ items =>
    Future.sequence(items.map{item =>
      Future(ItemDto(item.id, item.name, item.price, item.amount))
    })
  }

  override def getItem(id: Long): Future[Option[ItemDto]] =
    itemsDao.getItem(id).flatMap{
      case Some(item) => Future(ItemDto(item.id, item.name, item.price, item.amount)).map(Some.apply)
      case None => Future(None)
    }

  override def createItem(item: ItemCreationParametersDto): Future[ItemDto] =
    itemsDao.createItem(item.name, item.price, item.amount).flatMap{ item =>
      Future(ItemDto(item.id, item.name, item.price, item.amount))
    }

  override def addExistingItems(id: Long, addedAmount: Long): Future[Option[ItemDto]] =
    itemsDao.addExistingItems(id, addedAmount).flatMap{
      case true => getItem(id)
      case false => Future.successful(None)
    }

  override def subtractExistingItems(id: Long, subtractedAmount: Long): Future[Option[ItemDto]] =
    itemsDao.subtractExistingItems(id, subtractedAmount).flatMap{
      case true => getItem(id)
      case false => Future.failed(new RuntimeException("Number of items is too small to continue"))
    }

}
