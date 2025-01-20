package com.example.Modeme.Manager.Entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;
	
@Data
@Entity
@Table(name = "AddItem")
public class AddItem {
		 @Id
		 @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_seq_gen")
		 @SequenceGenerator(name = "item_seq_gen", sequenceName = "item_SEQ", allocationSize = 1)
		 private Long id;
		 
		 @Column(nullable = false)
		 private String name; // 상품 이름
		 
		 @Column(nullable = false)
		 private int stock; // 상품 수량
		 
		 @Column(nullable = false)
		 private int price; // 상품 가격
		 
		 @ElementCollection
		 @CollectionTable(name = "item_colors", joinColumns = @JoinColumn(name = "add_item_id"))
		 @Column(name = "color")
		 private List<String> colors; // 상품 색상

		  @ElementCollection
		  @CollectionTable(name = "item_color_names", joinColumns = @JoinColumn(name = "add_item_id"))
		  @Column(name = "color_name")
		  private List<String> colorNames; // 색상 이름

		 @Column(nullable = false)
		 private String category; // 메인 카테고리
		 
		 private String subcategory; // 서브 카테고리
		 
		 private String productSize; // 상품 사이즈
		 
//		  @OneToMany(mappedBy = "addItem", cascade = CascadeType.ALL, orphanRemoval = true)
//		  private List<ProductImage> images; // 상품 이미지 리스트
//		 
		 @Lob
		 @Column(nullable = false)
		 private String productDescription;
		 
}