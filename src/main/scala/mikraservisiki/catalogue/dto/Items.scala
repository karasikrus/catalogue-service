package mikraservisiki.catalogue.dto

import play.api.libs.json.{Format, Json}

object Items {

  case class ItemDto(id: Long, name: String,  price: Double, amount: Long)

  case class ItemCreationParametersDto(name: String, price: Double,  amount: Long)

  case class ItemAdditionParametersDto(id: Long, amount: Long)



  implicit val itemDtoFormat: Format[ItemDto] = Json.format[ItemDto]
  implicit val itemAdditionParametersDtoFormat: Format[ItemAdditionParametersDto] = Json.format[ItemAdditionParametersDto]
  implicit val itemCreationParametersDtoFormat: Format[ItemCreationParametersDto] = Json.format[ItemCreationParametersDto]

}
