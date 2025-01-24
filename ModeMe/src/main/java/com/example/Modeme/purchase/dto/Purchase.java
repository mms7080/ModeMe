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
	
//	@Column(nullable = false)
//	private int productPrice; // 상품가격 - 상품정보에서 가져오기?
	
	@Column(nullable = false)
	private int productMany; // 상품갯수
	
//	private int deliveryFee; // 배송비
	
//	private String isHearted; // 관심상품유무 - 상품정보에서 가져오기?
	
	@Column(nullable = false)
	private String address; // 기본주소 
	
	@Column(nullable = false)
	private String addressDetail; // 상세주소
	
	@Column(nullable = false)
	private int totalPrice; // 전체금액
	
	@Column(nullable = false)
	private LocalDateTime orderDate = LocalDateTime.now();
	
//	@Column(nullable = false)
//	private String impUid;
//	
//	@Column(nullable = false)
//	private String merchantUid;
}
