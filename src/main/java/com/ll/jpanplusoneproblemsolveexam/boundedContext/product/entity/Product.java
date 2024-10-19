package com.ll.jpanplusoneproblemsolveexam.boundedContext.product.entity;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;

import com.ll.jpanplusoneproblemsolveexam.boundedContext.category.entity.Category;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Product{
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	private String name;
	private Integer price;

	@ManyToOne(fetch = LAZY)
	private Category category;
}