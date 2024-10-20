package com.ll.jpanplusoneproblemsolveexam.boundedContext.category.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ll.jpanplusoneproblemsolveexam.boundedContext.product.entity.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class CategoryInfo {
	@JsonProperty
	private Long id;
	@JsonProperty
	private String name;
	@JsonProperty
	private Integer price;

	public static List<CategoryInfo> getAllCategoryInfo(List<Product> products) {
		return products
			.stream()
			.map(product -> new CategoryInfo(product.getId(), product.getName(), product.getPrice()))
			.collect(Collectors.toList());
	}
}
