package com.example.Modeme.prdDetail.entity;

import java.time.LocalDateTime;

import com.example.Modeme.User.UserEntity.User;

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
@Table(name="orders")
@SequenceGenerator(name = "OrderSeq", sequenceName = "OrderSeq", allocationSize = 1, initialValue = 1)
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OrderSeq")
	@Column(name = "order_id")
	private long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	@Column(nullable = false)
	private int total_price;
	
	@Column(name = "ordered_date", nullable = false)
	private LocalDateTime orderedDate = LocalDateTime.now();
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private OrderStatus status;
	
	public enum OrderStatus {
		PENDING,    // 주문 대기
		COMPLETED,  // 주문 완료
		CANCELED    // 주문 취소
	}
}
