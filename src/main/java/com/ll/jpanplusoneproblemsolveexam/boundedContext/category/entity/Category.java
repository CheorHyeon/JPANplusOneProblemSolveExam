package com.ll.jpanplusoneproblemsolveexam.boundedContext.category.entity;

import static jakarta.persistence.GenerationType.*;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.BatchSize;

import com.ll.jpanplusoneproblemsolveexam.boundedContext.product.entity.Product;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Category {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	private String name;

	@OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
	@Builder.Default
	@BatchSize(size = 10)
	List<Product> products = new ArrayList<>();
}
