package com.example.Modeme.prdDetail.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name="product_table")
@SequenceGenerator(
        name = "ProductSeq",
        sequenceName = "ProductSeq",
        allocationSize = 1,
        initialValue = 1
)
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ProductSeq")
	@Column(name="product_id")
	private Long id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private int price;
	
	@Column(nullable = false)
	private int stock;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "category_id")
	private Category category;
	
	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "size_id")
	private Size size;
	
	@Column(nullable = false)
	private String color;
}


