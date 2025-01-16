package com.example.Modeme.prdDetail.entity;

import java.util.List;
import com.example.Modeme.User.UserEntity.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "PRODUCT_BOARD")
@SequenceGenerator(
        name = "ProductBoardSeq",
        sequenceName = "ProductBoardSeq",
        allocationSize = 1,
        initialValue = 1
)
public class ProductBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ProductBoardSeq")
    @Column(name = "product_board_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany(mappedBy = "productBoard", cascade = CascadeType.ALL)
    private List<ProductReview> review;

    @OneToMany(mappedBy = "productBoard", cascade = CascadeType.ALL)
    private List<ProductQA> qa;

    @Column(name = "content_path", nullable = true)
    private String contentPath;

    @Column(name = "main_image_path", nullable = true)
    private String mainImagePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User users;
}
