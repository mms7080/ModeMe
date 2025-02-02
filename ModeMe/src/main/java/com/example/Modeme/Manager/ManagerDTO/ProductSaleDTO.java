package com.example.Modeme.Manager.ManagerDTO;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSaleDTO {
		private Long id;            // 주문 ID
	    private Date orderDate;   // 주문 일시
	    private String category;    // 상품 카테고리
	    private String itemname;    // 상품명
	    private int productMany;    // 수량
	    private int totalPrice;     // 총 금액
	    private String username;    // 주문 아이디
	    private String name;
	    private String paymentMethod; // 결제 방법
	    private String process;      // 주문 상태 (예: 입금 전, 배송 준비 중 등)
	    
	    // 생성자
	    public ProductSaleDTO(Long id, Date orderDate, String category, String itemname,
	                          int productMany, int totalPrice, String username, String name, String process) {
	        this.id = id;
	        this.orderDate = orderDate;
	        this.category = category;
	        this.itemname = itemname;
	        this.name = name;
	        this.productMany = productMany;
	        this.totalPrice = totalPrice;
	        this.username = username;
	        this.process = process;
	    }
}
