package com.example.Modeme.Manager.ManagerDTO;

import java.util.List;

import com.example.Modeme.Manager.Entity.ItemSize;

public class ItemSizeDTO {
    private Long id;  // ItemSize 엔티티의 ID
    private List<ItemSize> itemSize;  // 사이즈 정보

    // Getter and Setter methods
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public List<ItemSize> getItemSize() { return itemSize; }
    public void setItemSize(List<ItemSize> itemSize) { this.itemSize = itemSize; }
}
