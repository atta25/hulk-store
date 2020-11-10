package com.example.hulkstore.service;

import com.example.hulkstore.domain.Product;
import com.example.hulkstore.dto.CartDTO;
import com.example.hulkstore.dto.ProductDTO;
import com.example.hulkstore.exception.InvalidProductOperationException;
import com.example.hulkstore.exception.InvalidPurchaseOperationException;
import com.example.hulkstore.repository.ProductRepository;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Component
public class ProductService {
    private final ProductRepository productRepository;
    private static final Logger logger = Logger.getLogger(ProductService.class.getName());

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getProducts() {
        return productRepository.getProducts();
    }

    public void addProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setDescription(productDTO.getDescription());
        product.setQuantity(productDTO.getQuantity());

        productRepository.addProduct(product);
    }

    public void deleteProduct(Integer id) {
        try {
            productRepository.deleteProduct(id);
        } catch (Exception e) {
            throw new InvalidProductOperationException("The product could not be removed", e);
        }
    }

    public void modifyProduct(ProductDTO productDTO) {
        try {
            productRepository.modifyProduct(productDTO);
        } catch (Exception e) {
            throw new InvalidProductOperationException("The product could not be modified", e);
        }
    }

    public Product findById(Integer id) {
        try {
            return productRepository.findById(id);
        } catch (Exception e) {
            throw new InvalidProductOperationException("The product could not be found", e);
        }
    }

    //For this iteration, it can only be purchased if there is stock for all products
    public void buy(CartDTO cartDTO) {
        List<Integer> ids = cartDTO.getItems().entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toList());
        List<Product> productsFound = productRepository.findAllById(ids);
        List<Product> purchasedProducts = new ArrayList<>();

        productsFound.forEach(productFound -> {
            Product productToBuy = getProductToBuy(productFound, cartDTO);
            purchasedProducts.add(productToBuy);
        });

        purchasedProducts.forEach(productRepository::addProduct);
    }

    private Product getProductToBuy(Product productFound, CartDTO cartDTO) {
        Integer quantityToBuy = cartDTO.getItems().get(productFound.getId());
        if (validateStock(productFound.getQuantity(), quantityToBuy)) {
            productFound.setQuantity(productFound.getQuantity() - quantityToBuy);
            return productFound;
        } else {
            logger.info("There was no stock for one of the products in the cart");
            throw new InvalidPurchaseOperationException();
        }
    }

    //It is validated that there is stock
    private boolean validateStock(Integer stock, Integer quantityToBuy) {
        return stock >= quantityToBuy;
    }
}
