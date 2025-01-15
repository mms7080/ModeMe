//package com.example.Modeme.prdDetail.entity;
//
//import java.io.File;
//import java.util.List;
//
//import com.example.Modeme.User.UserEntity.User;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.FetchType;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//import jakarta.persistence.SequenceGenerator;
//import jakarta.persistence.Table;
//
//@Entity
//@Table(name = "product_qa_table")
//@SequenceGenerator(name = "ProductReviewSeq", sequenceName = "ProductReviewSeq", allocationSize = 1, initialValue = 1)
//public class ProductReview {
//	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ProductReviewSeq")
//	@Column(name = "review_id")
//	private long id;
//
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "product_board_id", nullable = false)
//	private ProductBoard productBoard;
//
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "user_id", nullable = false)
//	private User user;
//
//	@Column(nullable = false, length = 50)
//	private String title;
//
//	@Column(nullable = false, length = 500)
//	private String content;
//
//	private List<File> images;
//}
