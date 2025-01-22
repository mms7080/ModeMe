package com.example.Modeme.prdDetail.entity;

import java.util.List;

import com.example.Modeme.Manager.Entity.AddItem;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "PRODUCT_STOCK")
@SequenceGenerator(name = "ProductStockSeq", sequenceName = "ProductStockSeq", allocationSize = 1, initialValue = 1)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductStock {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ProductStockSeq")
    @Column(name = "stock_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "add_item_id", nullable = false)
    private AddItem additem;

    @Column(nullable = false)
    private String color;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "size_id", nullable = false) // 필드 이름 변경
//    private Fit sizes;
    
    @ElementCollection
    @CollectionTable(name = "item_sizes", joinColumns = @JoinColumn(name = "add_item_id"))
    @Column(name = "item_size")
    private List<String> productSizes;

    @Column(nullable = false)
    private int stock;
}
