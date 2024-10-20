package com.ll.jpanplusoneproblemsolveexam.boundedContext.category.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ll.jpanplusoneproblemsolveexam.boundedContext.category.dto.ResponseCategoryDto;
import com.ll.jpanplusoneproblemsolveexam.boundedContext.category.repository.CategoryQuerydslRepository;
import com.ll.jpanplusoneproblemsolveexam.boundedContext.category.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {
	private final CategoryQuerydslRepository categoryQuerydslRepository;
	private final CategoryRepository categoryRepository;

	public Page<ResponseCategoryDto> N_Plus_One_쿼리_해결CaseThree_FetchJoinWithPageInOneToMany(){
		PageRequest pageRequest = PageRequest.of(0, 2);
		return categoryQuerydslRepository.findCategoriesWithProductsFetchJoinPaging("테스트 카테고리", pageRequest)
			.map(ResponseCategoryDto::getAllCategoryList);
	}

	public Page<ResponseCategoryDto> N_Plus_One_쿼리_해결CaseFour_BatchSizeAnnotationWithPageInOneToMany(){
		PageRequest pageRequest = PageRequest.of(0, 2);
		return categoryQuerydslRepository.findCategoriesWithProductsBatchSizeAnnotationPaging("테스트 카테고리", pageRequest)
			.map(ResponseCategoryDto::getAllCategoryList);
	}
}
