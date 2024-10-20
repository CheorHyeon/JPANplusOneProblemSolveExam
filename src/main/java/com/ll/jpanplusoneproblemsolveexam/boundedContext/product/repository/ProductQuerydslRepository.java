package com.ll.jpanplusoneproblemsolveexam.boundedContext.product.repository;

import static com.ll.jpanplusoneproblemsolveexam.boundedContext.category.entity.QCategory.*;
import static com.ll.jpanplusoneproblemsolveexam.boundedContext.product.entity.QProduct.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.ll.jpanplusoneproblemsolveexam.boundedContext.category.entity.Category;
import com.ll.jpanplusoneproblemsolveexam.boundedContext.product.dto.ProductInfo;
import com.ll.jpanplusoneproblemsolveexam.boundedContext.product.dto.ResponseProductDto;
import com.ll.jpanplusoneproblemsolveexam.boundedContext.product.entity.Product;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
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

	public Page<ProductInfo> findProductsByCategory(Category category, Pageable pageable) {
		List<ProductInfo> list = queryFactory
			.select(Projections.constructor(ProductInfo.class,
				product.id, product.name, product.price))
			.from(product)
			.where(product.category.eq(category))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> total = queryFactory.select(product.count())
			.from(product)
			.where(product.category.eq(category));

		return PageableExecutionUtils.getPage(list, pageable, total::fetchOne);
	}

	public Page<ProductInfo> findProductsByCategoryWithTuple(Category category, PageRequest pageable) {
		List<Tuple> tuples = queryFactory
			.select(product.id, product.name, product.price)
			.from(product)
			.where(product.category.eq(category))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> total = queryFactory.select(product.count())
			.from(product)
			.where(product.category.eq(category));

		// tuple -> DTO List
		List<ProductInfo> list = tuples.stream()
			.map(tuple -> new ProductInfo(
				tuple.get(product.id),
				tuple.get(product.name),
				tuple.get(product.price)
			))
			.collect(Collectors.toList());

		return PageableExecutionUtils.getPage(list, pageable, total::fetchOne);
	}

	public List<ResponseProductDto> findResponseProductsQuerydsl() {
		return queryFactory
			// 생성자 내부에서 프록시 객체를 사용하기에 N+1 발생
			.select(Projections.constructor(ResponseProductDto.class,
				product))
			.from(product)
			.fetch();
	}
}
