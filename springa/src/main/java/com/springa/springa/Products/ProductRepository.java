package com.springa.springa.Products;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    @Override
    @Query("SELECT p FROM ProductEntity p ORDER BY p.id DESC")
    List<ProductEntity> findAll();
    
}