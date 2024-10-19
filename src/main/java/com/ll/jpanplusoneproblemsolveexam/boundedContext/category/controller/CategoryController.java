package com.ll.jpanplusoneproblemsolveexam.boundedContext.category.controller;

import org.springframework.stereotype.Controller;

import com.ll.jpanplusoneproblemsolveexam.boundedContext.category.service.CategoryService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CategoryController {
	private final CategoryService categoryService;
}
