package com.example.hulkstore;

import com.example.hulkstore.repository.ProductRepository;
import com.example.hulkstore.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.logging.Logger;

@SpringBootApplication
public class HulkStoreApplication {
	private static ProductRepository productRepository;
	private static UserRepository userRepository;
	private static final Logger logger = Logger.getLogger(HulkStoreApplication.class.getName());

	public HulkStoreApplication(UserRepository userRepository, ProductRepository productRepository) {
		this.userRepository = userRepository;
		this.productRepository = productRepository;
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(HulkStoreApplication.class, args);
		logger.info("Loading initial data");
		userRepository.init();
		productRepository.init();
		logger.info("Initial data load finished");
	}
}
