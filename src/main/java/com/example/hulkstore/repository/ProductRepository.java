package com.example.hulkstore.repository;

import com.example.hulkstore.domain.Product;
import com.example.hulkstore.dto.ProductDTO;
import com.example.hulkstore.exception.InvalidProductOperationException;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
public class ProductRepository {
    private final ProductRepositoryInterface productRepositoryInterface;

    public ProductRepository(ProductRepositoryInterface productRepositoryInterface) {
        this.productRepositoryInterface = productRepositoryInterface;
    }

    public void init() {
        Product product1 = new Product("Glasses", 22);
        Product product2 = new Product("Comics", 34);
        Product product3 = new Product("Tshirts", 45);
        Product product4 = new Product("Toys", 40);
        productRepositoryInterface.save(product1);
        productRepositoryInterface.save(product2);
        productRepositoryInterface.save(product3);
        productRepositoryInterface.save(product4);
    }

    public List<Product> getProducts() {
        return Lists.newArrayList(productRepositoryInterface.findAll());
    }

    public synchronized void addProduct(Product product) {
        try {
            productRepositoryInterface.save(product);
        } catch (Exception e) {
            throw new InvalidProductOperationException("The product could not be added", e);
        }
    }

    public synchronized void deleteProduct(Integer id) {
        productRepositoryInterface.deleteById(id);
    }

    public Product findById(Integer id) {
        return productRepositoryInterface.findById(id).get();
    }

    public synchronized void modifyProduct(ProductDTO productDTO) {
        Optional<Product> product = productRepositoryInterface.findById(productDTO.getId());
        product.get().setDescription(productDTO.getDescription());
        product.get().setQuantity(productDTO.getQuantity());

        productRepositoryInterface.save(product.get());
    }

    public List<Product> findAllById(List<Integer> ids) {
         return Lists.newArrayList(productRepositoryInterface.findAllById(ids));
    }
}
