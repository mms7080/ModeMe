//package com.example.Modeme.Manager.Entity;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//import jakarta.persistence.Table;
//import lombok.Data;
//
//@Data
//@Entity
//@Table(name = "ProductImages")
//public class ProductImage {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false)
//    private String imageUrl; // 이미지 파일 경로
//
//    @ManyToOne
//    @JoinColumn(name = "AddItem", nullable = false) // 외래키
//    private AddItem addItem; // 해당 이미지를 포함한 상품
//}
