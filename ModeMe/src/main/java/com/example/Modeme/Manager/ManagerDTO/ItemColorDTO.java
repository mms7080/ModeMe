package com.example.Modeme.Manager.ManagerDTO;

import java.util.List;

import com.example.Modeme.Manager.Entity.ItemColor;

public class ItemColorDTO {
    private Long id;  // ItemColor 엔티티의 ID
    private List<ItemColor> color;  // 색상 정보

    // Getter and Setter methods
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public List<ItemColor> getColor() { return color; }
    public void setColor(List<ItemColor> color) { this.color = color; }
}