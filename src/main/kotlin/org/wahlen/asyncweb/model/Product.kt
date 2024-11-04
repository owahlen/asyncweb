package org.wahlen.asyncweb.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("product")
data class Product(
  @Id
  val id: Long? = null,
  val categoryId: Long,
  val name: String,
  val description: String,
  val price: Double
)
