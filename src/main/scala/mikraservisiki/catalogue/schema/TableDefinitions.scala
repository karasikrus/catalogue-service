package mikraservisiki.catalogue.schema

import mikraservisiki.catalogue.HasDbConfigProvider

object TableDefinitions {



  trait ItemsTable {
    self: HasDbConfigProvider =>

    import profile.api._

    val items: TableQuery[Items] = TableQuery[Items]

    class Items(tag: Tag) extends Table[Item](tag, "items"){
      def * = (id, name, price, amount) <> (Item.tupled, Item.unapply)

      def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

      def name = column[String]("name")

      def price  = column[Double]("price")

      def amount = column[Long]("amount")

    }
  }





  case class Item(id: Long, name: String, price: Double, amount: Long)

}
