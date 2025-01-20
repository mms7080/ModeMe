package com.example.Modeme.prdDetail.entity;

import java.util.ArrayList;
import java.util.List;

import com.example.Modeme.User.UserEntity.User;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "PRODUCT_QA")
@SequenceGenerator(name = "ProductQASeq", sequenceName = "ProductQASeq", allocationSize = 1, initialValue = 1)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductQA {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ProductQASeq")
    @Column(name = "qa_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_board_id", nullable = false)
    private ProductBoard productBoard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User users;

    @Enumerated(EnumType.STRING) // Enum을 String으로 저장
    @Column(nullable = false)
    private TitleCategory title; // 미리 정의된 카테고리 중 하나 선택

    @Column(nullable = false, length = 500)
    private String content;

    @ElementCollection // 파일 경로를 저장할 경우 사용
    @CollectionTable(name = "product_qa_images", joinColumns = @JoinColumn(name = "qa_id"))
    @Column(name = "image_path", nullable = true)
    private List<String> images = new ArrayList<>(); // 파일 경로를 문자열로 저장

    @Column(nullable = true)
    private String password;

    @Column(nullable = false)
    private boolean isSecret;

    public enum TitleCategory {
        상품문의, // 첫 번째 카테고리
        사이즈문의, // 두 번째 카테고리
        기타문의 // 세 번째 카테고리
    }
}
