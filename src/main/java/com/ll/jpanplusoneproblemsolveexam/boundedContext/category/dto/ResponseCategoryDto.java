package com.ll.jpanplusoneproblemsolveexam.boundedContext.category.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ll.jpanplusoneproblemsolveexam.boundedContext.category.entity.Category;

import lombok.Builder;

@Builder
public class ResponseCategoryDto {
	@JsonProperty
	private Long id;
	@JsonProperty
	private String name;
	@JsonProperty
	private List<CategoryInfo> products;

	public static ResponseCategoryDto getAllCategoryList(Category category) {
		return ResponseCategoryDto.builder()
			.id(category.getId())
			.name(category.getName())
			.products(CategoryInfo.getAllCategoryInfo(category.getProducts()))
			.build();
	}
}
