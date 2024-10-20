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

	@Test
	@DisplayName("N+1 쿼리 해결 case 0 - Join을 사용하면 Lazy여도 가져오지 않을까? 싶지만 여전히 N+1문제 발생")
	void N_Plus_One_쿼리_해결_CaseZero() {
		List<ResponseProductDto> productDtoList = productService.N_Plus_One_쿼리_해결CaseZero_하지만해결실패();
		assertThat(productDtoList.size()).isEqualTo(15);
	}

	@Test
	@DisplayName("N+1 쿼리 해결 case 1 - FetchJoin을 사용하면 조회 대상 엔티티 + 연관 관계 엔티티 모두 가져온다.")
	void N_Plus_One_쿼리_해결_CaseOne() {
		List<ResponseProductDto> productDtoList = productService.N_Plus_One_쿼리_해결CaseOne_FetchJoinInManyToOne();
		assertThat(productDtoList.size()).isEqualTo(15);
	}

}
