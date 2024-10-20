package com.ll.jpanplusoneproblemsolveexam.boundedContext.product.repository;

import static com.ll.jpanplusoneproblemsolveexam.boundedContext.category.entity.QCategory.*;
import static com.ll.jpanplusoneproblemsolveexam.boundedContext.product.entity.QProduct.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.ll.jpanplusoneproblemsolveexam.boundedContext.product.entity.Product;
import com.querydsl.jpa.impl.JPAQuery;
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

	public List<Product> findAllProductsQuerydslWithFetchJoin() {
		return queryFactory
			.select(product)
			.from(product)
			.join(product.category, category).fetchJoin()
			.fetch();
	}

	public Page<Product> findPageProductsQuerydslWithFetchJoin(Pageable pageable) {
		List<Product> products = queryFactory
			.select(product)
			.from(product)
			.join(product.category, category).fetchJoin()
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> total = queryFactory.select(product.count())
			.from(product);

		return PageableExecutionUtils.getPage(products, pageable, total::fetchOne);
	}
}
