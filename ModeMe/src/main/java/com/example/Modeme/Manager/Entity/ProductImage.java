package com.example.Modeme.Manager.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ProductImage")
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String imageUrl; // GCS에 저장된 이미지 URL

    @ManyToOne
    @JoinColumn(name = "add_item_id")
    private AddItem addItem; // 상품과 연결

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public AddItem getAddItem() {
        return addItem;
    }

    public void setAddItem(AddItem addItem) {
        this.addItem = addItem;
    }
}
