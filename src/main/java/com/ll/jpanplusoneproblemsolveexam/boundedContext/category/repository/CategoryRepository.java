package com.ll.jpanplusoneproblemsolveexam.boundedContext.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.jpanplusoneproblemsolveexam.boundedContext.category.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
