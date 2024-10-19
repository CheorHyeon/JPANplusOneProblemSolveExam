package com.ll.jpanplusoneproblemsolveexam.boundedContext.product.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.jpanplusoneproblemsolveexam.boundedContext.product.dto.ResponseProductDto;
import com.ll.jpanplusoneproblemsolveexam.boundedContext.product.repository.ProductQuerydslRepository;
import com.ll.jpanplusoneproblemsolveexam.boundedContext.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
	private final ProductQuerydslRepository productQuerydslRepository;
	private final ProductRepository productRepository;

	@Transactional(readOnly = true)
	public List<ResponseProductDto> NPlusOne문제발생Querydsl(){
		return productQuerydslRepository.findAllProductsQuerydsl()
			.stream()
			.map(ResponseProductDto::getAllProductList)
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public List<ResponseProductDto> NPlusOne문제발생JPA(){
		return productRepository.findAll()
			.stream()
			.map(ResponseProductDto::getAllProductList)
			.collect(Collectors.toList());
	}
}
