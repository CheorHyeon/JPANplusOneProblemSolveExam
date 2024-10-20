package com.ll.jpanplusoneproblemsolveexam;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.ll.jpanplusoneproblemsolveexam.boundedContext.category.dto.ResponseCategoryDto;
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

	@Test
	@DisplayName("N+1 쿼리 해결 case 2 - FetchJoin과 Pageable을 ManyToOne에서 사용해도 Offset과 Limit이 쿼리로 잘 적용 된다.")
	void N_Plus_One_쿼리_해결_CaseTwo() {
		Page<ResponseProductDto> productDtoList = productService.N_Plus_One_쿼리_해결CaseTwo_FetchJoinWithPageInManyToOne();
		assertThat(productDtoList.getContent().size()).isEqualTo(2);
	}

	@Test
	@DisplayName("N+1 쿼리 해결 case 3 - FetchJoin과 Pageable을 OneTwoMany에서 사용시 "
		+ "DB의 관점과 Entity의 관점이 다르기 때문에 offset과 limit이 동작하지 않는다."
		+ "즉, 메모리에 전체 데이터를 로드하고 페이징을 하기 때문에 메모리 사용량이 증가한다.")
	void N_Plus_One_쿼리_해결_CaseThree() throws JsonProcessingException {
		Page<ResponseCategoryDto> responseCategoryDtos = categoryService.N_Plus_One_쿼리_해결CaseThree_FetchJoinWithPageInOneToMany();
		assertThat(responseCategoryDtos.getContent().size()).isEqualTo(2);

		// ObjectMapper를 이용해 Page 객체를 JSON으로 변환
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // pretty print
		// Page 객체를 JSON으로 변환
		String jsonResult = objectMapper.writeValueAsString(responseCategoryDtos);
		// JSON 결과 출력
		System.out.println(jsonResult);
	}
}
