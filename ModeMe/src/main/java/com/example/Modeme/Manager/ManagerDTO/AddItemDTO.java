package com.example.Modeme.Manager.ManagerDTO;

import java.util.List;


//import com.example.Modeme.Manager.Entity.ProductImage;

public class AddItemDTO {
    private Long id;
    private String name;
    private int stock;
    private int price;
    private List<String> colors;
    private List<String> colorNames;
	private String category;
	private String subcategory;
    private String productSize;
//    private List<ProductImage> images;
    private String productDescription;


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

    public String getProductSize() { return productSize; }
    public void setProductSize(String productSize) { this.productSize = productSize; }

//    public List<ProductImage> getImages() { return images; }
//    public void setImages(List<ProductImage> images) { this.images = images; }

    public String getProductDescription() { return productDescription; }
    public void setProductDescription(String productDescription) { this.productDescription = productDescription; }
	
}
