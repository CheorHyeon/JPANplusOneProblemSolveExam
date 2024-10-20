package com.ll.jpanplusoneproblemsolveexam.boundedContext.product.repository;

import static com.ll.jpanplusoneproblemsolveexam.boundedContext.category.entity.QCategory.*;
import static com.ll.jpanplusoneproblemsolveexam.boundedContext.product.entity.QProduct.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ll.jpanplusoneproblemsolveexam.boundedContext.product.entity.Product;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProductQuerydslRepository {
	private final JPAQueryFactory queryFactory;

	public List<Product> findAllProductsQuerydsl() {
		return queryFactory
			.selectFrom(product)
			.fetch();
	}

	public List<Product> findAllProductsQuerydslWithJoin() {
		return queryFactory
			.select(product)
			.from(product)
			.join(product.category, category)
			.fetch();
	}

}
