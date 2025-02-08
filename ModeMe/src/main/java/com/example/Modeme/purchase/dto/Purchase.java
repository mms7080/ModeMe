package com.example.Modeme.purchase.dto;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "PURCHASE")
@SequenceGenerator(
		name = "PURCHASE_SEQ",
		sequenceName = "PURCHASE_SEQ",
		allocationSize = 1,
		initialValue = 1)
public class Purchase {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PURCHASE_SEQ")
    private Long id; // 주문번호
	
	@Column(nullable = false)
	private int userId; // 유저PK
	
	@Column(nullable = false)
	private int productNumber; // 상품PK
	
	@Column(nullable = false)
	private int productMany; // 상품갯수
	
	@Column(nullable = false)
	private String address; // 기본주소 
	
	@Column(nullable = false)
	private String addressDetail; // 상세주소
	
	@Column(nullable = false)
	private int totalPrice; // 전체금액
	
	@Column(nullable = false)
	private LocalDateTime orderDate = LocalDateTime.now();
	
	@Column(nullable=false)
	private String itemname; //상품 이름
	
	@Column
	private String process; //주문 상태처리 (입금전, 배송완료 등등)
	
	@Column
	private String username; //유저 아이디
	
	@Column
	private String colorId; // 구매한 상품의 색상
	
	@Column
	private String sizeId; // 구매한 상품의 사이즈
	
	@Column
	private String merchantUid; // 동시에 주문했을 경우를 위한 구분자?

}
