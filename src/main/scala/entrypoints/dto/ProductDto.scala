package entrypoints.dto

case class ProductDTO(id: Long, name: String, price: BigDecimal)

object ProductDTO {
  def fromDomain(product: domain.models.Product): ProductDTO =
    ProductDTO(product.id, product.name, product.price)
}
