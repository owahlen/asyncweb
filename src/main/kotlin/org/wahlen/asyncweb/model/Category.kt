package org.wahlen.asyncweb.model

import org.springframework.data.relational.core.mapping.Table

@Table("category")
data class Category(
    val id: Long? = null,
    val name: String
)