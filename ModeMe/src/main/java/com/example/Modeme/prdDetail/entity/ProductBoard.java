package com.example.Modeme.prdDetail.entity;

import java.io.File;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name="product_board_table")
@SequenceGenerator(
        name = "ProductBoardSeq",
        sequenceName = "ProductBoardSeq",
        allocationSize = 1,
        initialValue = 1
)
public class ProductBoard {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ProductBoardSeq")
	@Column(name="prd_bord_id")
	private Long id;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name="product_id")
	private Product product;
	
	@OneToMany(mappedBy = "productBoard", cascade = CascadeType.ALL)
	@JoinColumn(name="review_id")
	private ProductReview review;

	@OneToMany(mappedBy = "productBoard", cascade = CascadeType.ALL)
	@JoinColumn(name="qa_id")
	private ProductQA qa;
	
	private File content;
	private File mainImage;
	
	@JoinTable(name = "user_table")
	private String user;
	
}
