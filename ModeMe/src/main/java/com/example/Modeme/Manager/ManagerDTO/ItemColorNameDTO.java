package com.example.Modeme.Manager.ManagerDTO;

import java.util.List;

import com.example.Modeme.Manager.Entity.ItemColorName;

public class ItemColorNameDTO {
    private Long id;  // ItemColorName 엔티티의 ID
    private List<ItemColorName> colorName;  // 색상 이름

    // Getter and Setter methods
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public List<ItemColorName> getColorName() { return colorName; }
    public void setColorName(List<ItemColorName> colorName) { this.colorName = colorName; }
}