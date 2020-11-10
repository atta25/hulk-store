package com.example.hulkstore.controller;

import com.example.hulkstore.domain.Product;
import com.example.hulkstore.dto.CartDTO;
import com.example.hulkstore.dto.ProductDTO;
import com.example.hulkstore.exception.InvalidProductOperationException;
import com.example.hulkstore.exception.InvalidPurchaseOperationException;
import com.example.hulkstore.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.logging.Logger;

@Controller
public class ProductController {
    private final ProductService productService;
    private static final Logger logger = Logger.getLogger(ProductController.class.getName());
    private static final String modalTemplate = "modal";
    private static final String modifyTemplate = "modify";
    private static final String stockTemplate = "stock";
    private static final String productsTemplate = "products";
    private static final String errorTemplate = "error";

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    //Product list is displayed
    @GetMapping("/products")
    public String getIndex(Model model) {
        model.addAttribute("products", productService.getProducts());

        return productsTemplate;
    }

    //A product is added, if this is not possible, an error is displayed
    @PostMapping("/products")
    public String addProduct(ProductDTO productDTO, Model model) {
        try {
            logger.info(String.format("Adding product with description=%s and quantity=%s", productDTO.getDescription(), productDTO.getQuantity()));
            productService.addProduct(productDTO);
            logger.info("Added product");
            model.addAttribute("message", String.format("The product with description: %s has been added successfully", productDTO.getDescription()));
            model.addAttribute("back", "products");

            return modalTemplate;
        } catch (InvalidProductOperationException e) {
            addAttributes(model, "products", "The product you are trying to load could not be entered","The product was not entered");

            return errorTemplate;
        }
    }

    //TODO: change http get method to delete
    //A product is deleted, if this is not possible, an error is displayed
    @GetMapping("/products/{id}")
    public String deleteProduct(@PathVariable("id") Integer id, Model model) {
        try {
            logger.info(String.format("Removing product with id=%s", id));
            productService.deleteProduct(id);
            logger.info("Product removed");

            model.addAttribute("message", String.format("The product with id: %s has been deleted", id));
            model.addAttribute("back", "products");

            return modalTemplate;
        } catch (InvalidProductOperationException e) {
            addAttributes(model, "products", "The product you were trying to remove could not be removed", "The product was not removed");

            return errorTemplate;
        }
    }

    //The product modification form is displayed
    @GetMapping("/products/modify/{id}")
    public String modifyProduct(@PathVariable("id") Integer id, Model model) {
        try {
            logger.info(String.format("Modifying product with id=%s", id));
            Product product = productService.findById(id);
            model.addAttribute("id", product.getId());
            model.addAttribute("description", product.getDescription());
            model.addAttribute("quantity", product.getQuantity());

            return modifyTemplate;
        } catch (InvalidProductOperationException e) {
            addAttributes(model, "products", "The product cannot be modified at this time, please try later", "Could not get the product to modify");

            return errorTemplate;
        }
    }

    //A product is modified, if this is not possible, an error is displayed
    @PostMapping("/products/modify")
    public String modifyProduct(ProductDTO productDTO, Model model) {
        try {
            logger.info(String.format("Modifying product with id=%s", productDTO.getId()));
            productService.modifyProduct(productDTO);
            logger.info(String.format("Product with id=%s modified", productDTO.getId()));
            model.addAttribute("message", String.format("The product with id: %s has been modified", productDTO.getId()));
            model.addAttribute("back", "products");

            return modalTemplate;
        } catch (InvalidPurchaseOperationException e) {
            addAttributes(model, "products", "The product you were trying to modify could not be modified", "The product was not modified");

            return errorTemplate;
        }
    }

    //Product stock is displayed
    @GetMapping("/stock")
    public String stock(Model model) {
        model.addAttribute("products", productService.getProducts());

        return stockTemplate;
    }

    //A purchase is made and stock discounts are made, if not possible, an error is displayed
    @PatchMapping("/products/buy")
    public String buy(CartDTO cartDTO, Model model) {
        try {
            logger.info("Buying products");
            productService.buy(cartDTO);
            logger.info("Product purchase successfully completed");
            model.addAttribute("back", "stock");
            model.addAttribute("message", "Thanks for your purchase");

            return modalTemplate;
        } catch (InvalidPurchaseOperationException e) {
            addAttributes(model, "stock", "The purchase could not be made", "The purchase could not be made");

            return errorTemplate;
        }
    }

    private void addAttributes(Model model, String back, String message, String errorMessage) {
        logger.info(errorMessage);
        model.addAttribute("back", back);
        model.addAttribute("message", message);
    }
}