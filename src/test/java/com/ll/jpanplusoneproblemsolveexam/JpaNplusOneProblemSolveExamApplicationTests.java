package com.ll.jpanplusoneproblemsolveexam;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.ll.jpanplusoneproblemsolveexam.boundedContext.category.service.CategoryService;
import com.ll.jpanplusoneproblemsolveexam.boundedContext.product.dto.ResponseProductDto;
import com.ll.jpanplusoneproblemsolveexam.boundedContext.product.service.ProductService;


@SpringBootTest
@Transactional
class JpaNplusOneProblemSolveExamApplicationTests {

	@Autowired
	ProductService productService;

	@Autowired
	CategoryService categoryService;

	@Test
	void contextLoads() {
	}

	@Test
	@DisplayName("N+1 쿼리 발생하는지 확인용 - Querydsl")
	void N_Plus_One_쿼리_발생_Querydsl() {
		List<ResponseProductDto> productDtoList = productService.NPlusOne문제발생Querydsl();
		assertThat(productDtoList.size()).isEqualTo(15);
	}

	@Test
	@DisplayName("N+1 쿼리 발생하는지 확인용 - JPA")
	void N_Plus_One_쿼리_발생_JPA() {
		List<ResponseProductDto> productDtoList = productService.NPlusOne문제발생JPA();
		assertThat(productDtoList.size()).isEqualTo(15);
	}

}
