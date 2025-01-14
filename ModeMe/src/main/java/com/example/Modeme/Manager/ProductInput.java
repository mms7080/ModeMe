package com.example.Modeme.Manager;

import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PRODUCTINPUT")
@SequenceGenerator(
		allocationSize = 1,
		initialValue = 0,
		name = "NewInputNum",
		sequenceName = "NewInputNum"
	)
public class ProductInput {

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NewInputNum")
    private Long id;
	private String name; // 상품이름
	private int stock; // 상품 수량
	private int price; // 상품 가격
	private String color; // 상품 색상
	private String colorName; // 색상 이름
	private String category; // 메인 카테고리
	private String subCategory; // 서브 카테고리
	private String size; // 상품 사이즈
    private String productDescription; // 상품 상세정보
    	//?
    // 이미지 경로를 저장하는 List
    @ElementCollection
    private List<String> imagePaths; // 이미지 파일 경로
		
}
