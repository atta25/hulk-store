package com.example.hulkstore.repository;

import com.example.hulkstore.domain.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepositoryInterface extends CrudRepository<Product, Integer> {
}