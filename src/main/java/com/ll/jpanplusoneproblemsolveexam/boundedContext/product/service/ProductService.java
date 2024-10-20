package com.ll.jpanplusoneproblemsolveexam.boundedContext.product.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.jpanplusoneproblemsolveexam.boundedContext.category.entity.Category;
import com.ll.jpanplusoneproblemsolveexam.boundedContext.category.repository.CategoryRepository;
import com.ll.jpanplusoneproblemsolveexam.boundedContext.product.dto.ProductInfo;
import com.ll.jpanplusoneproblemsolveexam.boundedContext.product.dto.ResponseProductDto;
import com.ll.jpanplusoneproblemsolveexam.boundedContext.product.repository.ProductQuerydslRepository;
import com.ll.jpanplusoneproblemsolveexam.boundedContext.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
	private final ProductQuerydslRepository productQuerydslRepository;
	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;

	public List<ResponseProductDto> NPlusOne문제발생Querydsl(){
		return productQuerydslRepository.findAllProductsQuerydsl()
			.stream()
			.map(ResponseProductDto::getAllProductList)
			.collect(Collectors.toList());
	}

	public List<ResponseProductDto> NPlusOne문제발생JPA(){
		return productRepository.findAll()
			.stream()
			.map(ResponseProductDto::getAllProductList)
			.collect(Collectors.toList());
	}

	public List<ResponseProductDto> N_Plus_One_쿼리_해결CaseZero_하지만해결실패(){
		return productQuerydslRepository.findAllProductsQuerydslWithJoin()
			.stream()
			.map(ResponseProductDto::getAllProductList)
			.collect(Collectors.toList());
	}

	public List<ResponseProductDto> N_Plus_One_쿼리_해결CaseOne_FetchJoinInManyToOne(){
		return productQuerydslRepository.findAllProductsQuerydslWithFetchJoin()
			.stream()
			.map(ResponseProductDto::getAllProductList)
			.collect(Collectors.toList());
	}

	public Page<ResponseProductDto> N_Plus_One_쿼리_해결CaseTwo_FetchJoinWithPageInManyToOne(){
		PageRequest pageRequest = PageRequest.of(1, 2);
		return productQuerydslRepository.findPageProductsQuerydslWithFetchJoin(pageRequest)
			.map(ResponseProductDto::getAllProductList);
	}

	public Page<ProductInfo> N_Plus_One_쿼리_해결CaseFive_findDtoBySpecificColumns(){
		Category category = categoryRepository.findById(1L).orElse(null);
		PageRequest pageRequest = PageRequest.of(1, 2);
		return productQuerydslRepository.findProductsByCategory(category, pageRequest);
	}
}
