//package com.example.Modeme.prdDetail.entity;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.FetchType;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//import jakarta.persistence.OneToMany;
//import jakarta.persistence.SequenceGenerator;
//
//@Entity
//@SequenceGenerator(name = "OrderItemSeq", sequenceName = "OrderItemSeq", allocationSize = 1, initialValue = 1)
//public class OrderItem {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OrderItemSeq")
//    @Column(name = "order_item_id")
//    private long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "order_id", nullable = false)
//    private Order order;
//
//    @ManyToOne(fetch = FetchType.EAGER) // @OneToMany에서 @ManyToOne으로 변경
//    @JoinColumn(name = "product_id", nullable = false)
//    private Product product;
//
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "size_id", nullable = false)
//    private Size size;
//
//    @Column(nullable = false)
//    private int quantity;
//
//    @Column(nullable = false)
//    private int price;
//}
