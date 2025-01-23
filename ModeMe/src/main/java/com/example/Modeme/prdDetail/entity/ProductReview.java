package com.example.Modeme.prdDetail.entity;

import java.util.ArrayList;
import java.util.List;

import com.example.Modeme.Manager.Entity.AddItem;
import com.example.Modeme.User.UserEntity.User;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "PRODUCT_REVIEW")
@SequenceGenerator(name = "ReviewSeq", sequenceName = "ReviewSeq", allocationSize = 1, initialValue = 1)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductReview {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ReviewSeq")
	@Column(name = "review_id")
	private long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "add_item_id", nullable = false)
    private AddItem addItem;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
	private User users;

	@Column(nullable = false, length = 50)
	private String title;

	@Column(nullable = false, length = 500)
	private String content;

	@ElementCollection
	@CollectionTable(name = "review_images", joinColumns = @JoinColumn(name = "review_id"))
	@Column(name = "image_path", nullable = true)
	private List<String> images = new ArrayList<>();
}
