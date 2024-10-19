package com.ll.jpanplusoneproblemsolveexam.boundedContext.category.service;

import org.springframework.stereotype.Service;

import com.ll.jpanplusoneproblemsolveexam.boundedContext.category.repository.CategoryQuerydslRepository;
import com.ll.jpanplusoneproblemsolveexam.boundedContext.category.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {
	private final CategoryQuerydslRepository categoryQuerydslRepository;
	private final CategoryRepository categoryRepository;
}
