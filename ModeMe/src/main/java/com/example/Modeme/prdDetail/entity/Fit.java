package com.example.Modeme.prdDetail.entity;

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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "PRODUCT_SIZE")
@SequenceGenerator(
        name = "SizeSeq",
        sequenceName = "SizeSeq",
        allocationSize = 1,
        initialValue = 1
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Fit {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SizeSeq")
    @Column(name = "product_size_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "sizes")
    private SizeType sizes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;  // 수정 없음

    public enum SizeType {
        S, M, L, XL, FREESIZE
    }
}

