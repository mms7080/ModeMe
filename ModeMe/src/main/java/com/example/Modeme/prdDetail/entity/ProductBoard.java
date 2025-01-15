//package com.example.Modeme.prdDetail.entity;
//
//import java.io.File;
//import java.util.List;
//
//import com.example.Modeme.User.UserEntity.User;
//
//import jakarta.persistence.CascadeType;
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.FetchType;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//import jakarta.persistence.OneToMany;
//import jakarta.persistence.OneToOne;
//import jakarta.persistence.SequenceGenerator;
//import jakarta.persistence.Table;
//
//@Entity
//@Table(name = "product_board_table")
//@SequenceGenerator(
//        name = "ProductBoardSeq",
//        sequenceName = "ProductBoardSeq",
//        allocationSize = 1,
//        initialValue = 1
//)
//public class ProductBoard {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ProductBoardSeq")
//    @Column(name = "prd_bord_id")
//    private Long id;
//
//    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JoinColumn(name = "product_id")
//    private Product product;
//
//    @OneToMany(mappedBy = "productBoard", cascade = CascadeType.ALL)
//    private List<ProductReview> review; // 필드 타입을 List로 변경
//
////    @OneToMany(mappedBy = "productBoard", cascade = CascadeType.ALL)
////    private List<ProductQA> qa; // 필드 타입을 List로 변경
//
//    private File content;
//    private File mainImage;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;
//
//}
