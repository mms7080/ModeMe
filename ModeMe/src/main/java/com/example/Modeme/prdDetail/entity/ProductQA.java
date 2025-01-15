package com.example.Modeme.prdDetail.entity;

import java.io.File;
import java.lang.reflect.Array;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "product_qa_table")
@SequenceGenerator(name = "ProductQASeq", sequenceName = "ProductQASeq", allocationSize = 1, initialValue = 1)
public class ProductQA {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ProductQASeq")
	@Column(name = "qa_id")
	private long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="qa_id")
	private ProductBoard board;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="user_id")
	private String user;
	
    @Enumerated(EnumType.STRING) // Enum을 String으로 저장
    @Column(nullable = false)
    private TitleCategory title; // 미리 정의된 카테고리 중 하나 선택
    
    @Column(nullable = false, length = 500)
	private String content;
	
	private List<File> images;
	
	
	public enum TitleCategory {
	    상품문의, // 첫 번째 카테고리
	    사이즈문의, // 두 번째 카테고리
	    기타문의 // 세 번째 카테고리
	}
}



















