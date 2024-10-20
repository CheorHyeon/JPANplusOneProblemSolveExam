package com.ll.jpanplusoneproblemsolveexam.boundedContext.category.repository;

import static com.ll.jpanplusoneproblemsolveexam.boundedContext.category.entity.QCategory.*;
import static com.ll.jpanplusoneproblemsolveexam.boundedContext.product.entity.QProduct.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.ll.jpanplusoneproblemsolveexam.boundedContext.category.entity.Category;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CategoryQuerydslRepository {
	private final JPAQueryFactory queryFactory;

	public Page<Category> findCategoriesWithProductsFetchJoinPaging(String categoryName, Pageable pageable) {
		List<Category> results = queryFactory.selectFrom(category)
			.leftJoin(category.products, product).fetchJoin()
			.where(category.name.contains(categoryName))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> total = queryFactory.select(category.count())
			.from(category)
			.where(category.name.contains(categoryName));

		// 실제로는 메모리 내에서 페이징이 적용됨
		return PageableExecutionUtils.getPage(results, pageable, total::fetchOne);
	}

	public Page<Category> findCategoriesWithProductsBatchSizeAnnotationPaging(String categoryName, Pageable pageable) {
		// Product 없이 Category만 반환 -> DTO 변환 시 Entity 사용하기에 N+1 발생하나
		// @BatchSize로 인해 1개의 추가 쿼리만 발생 select product.* where categoryId in (...)
		List<Category> results = queryFactory.selectFrom(category)
			.where(category.name.contains(categoryName))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> total = queryFactory.select(category.count())
			.from(category)
			.where(category.name.contains(categoryName));

		return PageableExecutionUtils.getPage(results, pageable, total::fetchOne);
	}
}
