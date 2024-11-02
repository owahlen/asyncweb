package org.wahlen.asyncweb.repository

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.wahlen.asyncweb.model.Product

interface ProductRepository : CoroutineCrudRepository<Product, Long>
