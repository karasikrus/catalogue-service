package mikraservisiki.catalogue.dao

import java.time.temporal.TemporalAmount

import mikraservisiki.catalogue.HasDbConfigProvider
import mikraservisiki.catalogue.schema.TableDefinitions.{Item, ItemsTable}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait ItemsDao {


  def getItems: Future[Seq[Item]]



  def getItem(id: Long): Future[Option[Item]]

  def createItem(name: String, price: Double, amount: Long): Future[Item]


}

object RelationalItemsDao extends ItemsDao
  with HasDbConfigProvider
  with ItemsTable {

  import profile.api._
  override def getItems: Future[Seq[Item]] = db.run(items.result)

  override def getItem(id: Long): Future[Option[Item]] = db.run(items.filter(_.id === id).result.headOption)

  override def createItem(name: String, price: Double, amount: Long): Future[Item] = db.run{
    (items returning items) += Item(0, name, price, amount)
  }






 // override def addOrUpdateItem(item: Item): Future[Boolean] = db.run(items insertOrUpdate item).map(_ == 1)

 // override def getItemsList(orderId: Long): Future[Seq[Item]] = db.run(items.filter(_.orderId === orderId).result)
}