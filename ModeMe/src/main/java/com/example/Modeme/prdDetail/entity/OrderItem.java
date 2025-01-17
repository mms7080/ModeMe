package com.example.Modeme.prdDetail.entity;

import com.example.Modeme.prdDetail.entity.ProductStock.SizeType;

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
@Table(name="ORDERS_ITEM")
@SequenceGenerator(name = "OrderItemSeq", sequenceName = "OrderItemSeq", allocationSize = 1, initialValue = 1)
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OrderItemSeq")
    @Column(name = "order_item_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER) // @OneToMany에서 @ManyToOne으로 변경
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_size", nullable = false) // 필드 이름 변경
    private SizeType productSize;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private int price;
}
