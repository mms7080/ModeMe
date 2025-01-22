package com.example.Modeme.Manager.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "item_size")
public class ItemSize {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_size_seq")
    @SequenceGenerator(name = "item_size_seq", sequenceName = "item_size_SEQ", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "add_item_id")
    private AddItem addItem;

    @Column(name = "item_size")
    private String itemSize;
}