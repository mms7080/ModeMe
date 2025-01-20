package com.example.Modeme.Manager.ManagerDTO;

import java.util.List;

// import com.example.Modeme.Manager.Entity.ProductImage;

public class AddItemDTO {
    private Long id;
    private String name;
    private int stock;
    private int price;
    private List<String> colors; // 상품 색상
    private List<String> colorNames; // 색상 이름
    private String category; // 메인 카테고리
    private String subcategory; // 서브 카테고리
    private List<String> productSizes; // 상품 사이즈 리스트 (변경됨)
    private String productDescription; // 상품 상세정보
    // private List<ProductImage> images; // 이미지 관련 (주석 처리된 상태 유지)

    // Getter and Setter methods
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public List<String> getColors() { return colors; }
    public void setColors(List<String> colors) { this.colors = colors; }

    public List<String> getColorNames() { return colorNames; }
    public void setColorNames(List<String> colorNames) { this.colorNames = colorNames; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getSubcategory() { return subcategory; }
    public void setSubcategory(String subcategory) { this.subcategory = subcategory; }

    public List<String> getProductSizes() { return productSizes; } // 수정된 부분
    public void setProductSizes(List<String> productSizes) { this.productSizes = productSizes; } // 수정된 부분

    public String getProductDescription() { return productDescription; }
    public void setProductDescription(String productDescription) { this.productDescription = productDescription; }

    // public List<ProductImage> getImages() { return images; }
    // public void setImages(List<ProductImage> images) { this.images = images; }
}
