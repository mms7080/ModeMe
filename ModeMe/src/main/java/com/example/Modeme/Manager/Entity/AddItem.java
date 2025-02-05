package com.example.Modeme.Manager.Entity;

import java.util.ArrayList;
import java.util.List;


import jakarta.persistence.*;

@Entity
@Table(name = "AddItem")
public class AddItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_seq_gen")
    @SequenceGenerator(name = "item_seq_gen", sequenceName = "item_SEQ", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String name; // 상품 이름

    @Column(nullable = false)
    private int stock; // 상품 수량

    @Column(nullable = false)
    private int price; // 상품 가격

    @OneToMany(mappedBy = "addItem", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<ItemColor> colors; // 상품 색상

    @OneToMany(mappedBy = "addItem", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<ItemColorName> colorNames; // 색상 이름

    @Column(nullable = false)
    private String category; // 메인 카테고리

    private String subcategory; // 서브 카테고리

    @OneToMany(mappedBy = "addItem", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<ItemSize> productSizes; // 상품 사이즈 리스트

    @Lob
    @Column(nullable = false)
    private String productDescription; // 상품 상세정보

    @ElementCollection // ✅ 여러 개의 이미지 URL 저장 가능
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "add_item_id"))
    @Column(name = "image_url")
    private List<String> imageUrls = new ArrayList<>();


    // Getter and Setter methods
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<ItemColor> getColors() {
        return colors;
    }

    public void setColors(List<ItemColor> colors) {
        this.colors = colors;
    }

    public List<ItemColorName> getColorNames() {
        return colorNames;
    }

    public void setColorNames(List<ItemColorName> colorNames) {
        this.colorNames = colorNames;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public List<ItemSize> getProductSizes() {
        return productSizes;
    }

    public void setProductSizes(List<ItemSize> productSizes) {
        this.productSizes = productSizes;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }
    // 추가 메서드
    public void addColor(ItemColor itemColor) {
        if (this.colors == null) {
            this.colors = new ArrayList<>();
        }
        this.colors.add(itemColor);
    }

    public void addColorName(ItemColorName itemColorName) {
        if (this.colorNames == null) {
            this.colorNames = new ArrayList<>();
        }
        this.colorNames.add(itemColorName);
    }

    public void addProductSize(ItemSize itemSize) {
        if (this.productSizes == null) {
            this.productSizes = new ArrayList<>();
        }
        this.productSizes.add(itemSize);
    }


	public List<String> getImageUrls() {
		return imageUrls;
	}

	public void setImageUrls(List<String> imageUrls) {
		this.imageUrls = imageUrls;
	}
}
