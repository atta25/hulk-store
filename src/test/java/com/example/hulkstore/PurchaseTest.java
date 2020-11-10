package com.example.hulkstore;

import com.example.hulkstore.domain.Product;
import com.example.hulkstore.dto.CartDTO;
import com.example.hulkstore.exception.InvalidPurchaseOperationException;
import com.example.hulkstore.repository.ProductRepository;
import com.example.hulkstore.service.ProductService;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import static org.mockito.Mockito.*;

public class PurchaseTest {
    private ProductRepository productRepositoryMock;
    private ProductService productService;

    @Before
    public void setUp() {
        productRepositoryMock = mock(ProductRepository.class);
        productService = new ProductService(productRepositoryMock);
    }

    @Test
    public void whenThereIsStockForAllProductsYouShouldBeAbleToBuy() {
        Map<Integer, Integer> items = new HashMap<>();
        items.put(1, 2);
        items.put(2, 3);
        CartDTO cartDTO = new CartDTO();
        cartDTO.setItems(items);

        Product productFound1 = new Product("desc1", 2);
        productFound1.setId(1);
        Product productFound2 = new Product("desc2", 5);
        productFound2.setId(2);

        when(productRepositoryMock.findAllById(Arrays.asList(1, 2))).thenReturn(Arrays.asList(productFound1, productFound2));

        productService.buy(cartDTO);

        verify(productRepositoryMock).addProduct(productFound1);
        verify(productRepositoryMock).addProduct(productFound2);
    }

    @Test(expected = InvalidPurchaseOperationException.class)
    public void whenThereIsNoStockForAllProductsShouldThrowAnException() {
        Map<Integer, Integer> items = new HashMap<>();
        items.put(1, 2);
        items.put(2, 2);
        CartDTO cartDTO = new CartDTO();
        cartDTO.setItems(items);

        Product productFound1 = new Product("desc1", 2);
        productFound1.setId(1);

        Product productFound2 = new Product("desc2", 1);
        productFound2.setId(2);

        when(productRepositoryMock.findAllById(Arrays.asList(1, 2))).thenReturn(Arrays.asList(productFound1, productFound2));

        productService.buy(cartDTO);
    }
}
