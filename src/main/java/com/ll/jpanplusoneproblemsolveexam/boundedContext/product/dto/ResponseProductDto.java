package com.ll.jpanplusoneproblemsolveexam.boundedContext.product.dto;

import com.ll.jpanplusoneproblemsolveexam.boundedContext.product.entity.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class ResponseProductDto{
	private Long id;
	private String name;
	private Integer price;
	private String categoryName;

	public static ResponseProductDto getAllProductList(Product product){
		return ResponseProductDto.builder()
			.id(product.getId())
			.name(product.getName())
			.price(product.getPrice())
			.categoryName(product.getCategory().getName()) // Proxy 객체에서 실제 객체 참조 필요하여 N개 추가 쿼리
			.build();
	}

	public ResponseProductDto(Product product){
		this.id = product.getId();
		this.name = product.getName();
		this.price = product.getPrice();
		this.categoryName = product.getCategory().getName();
	}
}