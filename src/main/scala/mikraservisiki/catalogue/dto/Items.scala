package mikraservisiki.catalogue.dto

import play.api.libs.json.{Json, OFormat}

object Items {

  case class ItemDto(id: Long, name: String,  price: Double, amount: Long)

  case class ItemCreationParametersDto(name: String, price: Double,  amount: Long)

  case class ItemAdditionParametersDto(id: Long, amount: Long)

  case class QueueOrderDto(orderId: Long)

  case class AmountDto(amount: Int)

  implicit val itemDtoFormat: OFormat[ItemDto] = Json.format[ItemDto]
  implicit val itemAdditionParametersDtoFormat: OFormat[ItemAdditionParametersDto] = Json.format[ItemAdditionParametersDto]
  implicit val itemCreationParametersDtoFormat: OFormat[ItemCreationParametersDto] = Json.format[ItemCreationParametersDto]
  implicit val queueOrderDtoFormat: OFormat[QueueOrderDto] = Json.format[QueueOrderDto]
  implicit val amountDtoFormat: OFormat[AmountDto] = Json.format[AmountDto]
}
