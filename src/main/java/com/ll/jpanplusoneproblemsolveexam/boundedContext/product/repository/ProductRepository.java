package com.ll.jpanplusoneproblemsolveexam.boundedContext.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.jpanplusoneproblemsolveexam.boundedContext.product.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
