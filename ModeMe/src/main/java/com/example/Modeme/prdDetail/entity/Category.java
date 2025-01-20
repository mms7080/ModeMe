package com.example.Modeme.prdDetail.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="CATEGORY")
@SequenceGenerator(
        name = "CategorySeq",
        sequenceName = "CategorySeq",
        allocationSize = 1,
        initialValue = 1
)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CategorySeq")
	@Column(name="category_id")
	private Long id;
	
    @Column(name = "category_name", nullable = false, length = 50) // name 칼럼 추가
    private String categoryName;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	private Category parent;
	
	@OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();
	
	// 연관관계 편의 메소드
    public void addChildCategory(Category child) {
        this.child.add(child);
        child.setParent(this);
    }
}
