package com.ll.jpanplusoneproblemsolveexam.boundedContext.product.controller;

import org.springframework.stereotype.Controller;

import com.ll.jpanplusoneproblemsolveexam.boundedContext.product.service.ProductService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ProductController {
	private final ProductService productService;
}
