package org.wahlen.asyncweb.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.wahlen.asyncweb.dto.ProductResponseDTO
import org.wahlen.asyncweb.model.Product

interface ProductRepository : CoroutineCrudRepository<Product, Long> {

    @Query(
        """SELECT p.id, p.category_id, c.name as category_name, p.name, p.price  
           FROM product p 
           INNER JOIN category c ON c.id = p.category_id"""
    )
    fun findAllProductResponseDTOs(): Flow<ProductResponseDTO>

}
