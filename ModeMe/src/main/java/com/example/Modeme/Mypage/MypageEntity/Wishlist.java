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
	private String userid; //사용자 아이디
	@Column
	private String image; //이미지
	@Column
	private String itemname; //상품명
	@Column
	private int price; //가격
	@Column
	private Long itemNumber; //상품번호

	
}
