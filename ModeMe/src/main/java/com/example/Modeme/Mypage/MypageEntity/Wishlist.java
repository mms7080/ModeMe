package com.example.Modeme.Mypage.MypageEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
	
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Wishlist")
@SequenceGenerator(
		allocationSize = 1,
		initialValue = 1,
		name = "WishlistSeq",
		sequenceName = "WishlistSeq"
	)
public class Wishlist {
	@Id
	@GeneratedValue(
			strategy = GenerationType.SEQUENCE,
			generator = "WishlistSeq"
		)
	private Long wishid;
	
	@Column
	private String userid;
	@Column
	private String image;
	@Column
	private String itemname;
	@Column
	private int price;
	@Column
	private Long itemNumber;

	
}
