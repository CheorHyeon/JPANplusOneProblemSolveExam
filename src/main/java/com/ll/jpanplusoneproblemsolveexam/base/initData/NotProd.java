package com.ll.jpanplusoneproblemsolveexam.base.initData;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ll.jpanplusoneproblemsolveexam.boundedContext.category.entity.Category;
import com.ll.jpanplusoneproblemsolveexam.boundedContext.category.repository.CategoryRepository;
import com.ll.jpanplusoneproblemsolveexam.boundedContext.product.entity.Product;
import com.ll.jpanplusoneproblemsolveexam.boundedContext.product.repository.ProductRepository;

@Configuration
public class NotProd {
	@Bean
	CommandLineRunner initData(CategoryRepository categoryRepository, ProductRepository productRepository) {
		return args -> {
			List<Category> categoryList = new ArrayList<>();
			for (int i = 0; i < 3; i++) {
				categoryList.add(Category
					.builder()
					.name("테스트 카테고리 " + i)
					.build());
			}

			categoryRepository.saveAll(categoryList);

			List<Product> productList = new ArrayList<>();
			for (int i = 0; i < 15; i++) {
				Category category = switch (i / 5) {
					case 0 -> categoryList.get(0); // 0~4
					case 1 -> categoryList.get(1); // 5~9
					default -> categoryList.get(2); // 10~14
				};
				productList.add(Product.builder()
					.name("테스트상품명 " + i)
					.price(1000)
					.category(category)
					.build());
			}

			productRepository.saveAll(productList);
		};
	}
}