package mikraservisiki.catalogue.schema

import mikraservisiki.catalogue.HasDbConfigProvider

object TableDefinitions {



  trait ItemsTable {
    self: HasDbConfigProvider =>

    import profile.api._

    val items: TableQuery[Items] = TableQuery[Items]

    class Items(tag: Tag) extends Table[Item](tag, "items"){
      def * = (id, name, price, initialAmount) <> (Item.tupled, Item.unapply)

      def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

      def name = column[String]("name")

      def price  = column[Double]("price")

      def initialAmount = column[Long]("initial_amount")

    }
  }

  trait ReservationsTable {
    self: HasDbConfigProvider =>

    import profile.api._

    val reservations: TableQuery[Reservations] = TableQuery[Reservations]

    class Reservations(tag: Tag) extends Table[Reservation](tag, "reservations") {
      def * = (itemId, orderId, amount) <> (Reservation.tupled, Reservation.unapply)

      def itemId = column[Long]("item_id")

      def orderId = column[Long]("order_id")

      def amount = column[Int]("amount")

    }

  }


  case class Item(id: Long, name: String, price: Double, initialAmount: Long)

  case class Reservation(itemId: Long, orderId: Long, amount: Int)
}
