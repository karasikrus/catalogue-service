package mikraservisiki.catalogue.dao

import mikraservisiki.catalogue.HasDbConfigProvider
import mikraservisiki.catalogue.schema.TableDefinitions.{Item, ItemsTable, Reservation, ReservationsTable}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


trait ItemsDao {


  def getItems: Future[Seq[Item]]

  def getItem(id: Long): Future[Option[Item]]

  def createItem(name: String, price: Double, amount: Long): Future[Item]

  def addExistingItems(id: Long, amount: Long): Future[Boolean]

  def getReservedAmount(itemId: Long): Future[Int]

  def reserveItems(reservation: Reservation): Future[Unit]

  def freeItems(orderId: Long): Future[Unit]
}

object RelationalItemsDao extends ItemsDao
  with HasDbConfigProvider
  with ItemsTable
  with ReservationsTable {

  import profile.api._
  override def getItems: Future[Seq[Item]] = db.run(items.result)

  override def getItem(id: Long): Future[Option[Item]] = db.run(items.filter(_.id === id).result.headOption)

  override def createItem(name: String, price: Double, amount: Long): Future[Item] = db.run{
    (items returning items) += Item(0, name, price, amount)
  }

  override def addExistingItems(id: Long, amount: Long): Future[Boolean] = db.run(
    sqlu"""UPDATE items SET amount = amount + $amount WHERE id = $id"""
  ).map(_ == 1)

  override def getReservedAmount(itemId: Long): Future[Int] =
    db.run(reservations.filter(_.itemId === itemId).map(_.amount).sum.getOrElse(0).result)

  override def reserveItems(reservation: Reservation): Future[Unit] =
    db.run(reservations insertOrUpdate reservation).map(_ => {})

  override def freeItems(orderId: Long): Future[Unit] =
    db.run(reservations.filter(_.orderId === orderId).delete).map(_ => {})
}