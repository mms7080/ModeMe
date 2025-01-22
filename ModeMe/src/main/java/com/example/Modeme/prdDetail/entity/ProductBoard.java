package com.example.Modeme.prdDetail.entity;

import java.util.List;

import com.example.Modeme.Manager.Entity.AddItem;
import com.example.Modeme.User.UserEntity.User;
import com.example.Modeme.prdDetail.entity.ProductQA.TitleCategory;

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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "PRODUCT_BOARD")
@SequenceGenerator(
        name = "ProductBoardSeq",
        sequenceName = "ProductBoardSeq",
        allocationSize = 1,
        initialValue = 1
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ProductBoardSeq")
    @Column(name = "product_board_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "AddItem_id")
    private AddItem AddItem;

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
