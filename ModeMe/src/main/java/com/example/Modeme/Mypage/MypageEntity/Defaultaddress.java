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
@Table(name = "DefaultAddress")
@SequenceGenerator(
		allocationSize = 1,
		initialValue = 1,
		name = "DefaultAddressSeq",
		sequenceName = "DefaultAddressSeq"
	)
public class Defaultaddress {
	@Id
	@GeneratedValue(
			strategy = GenerationType.SEQUENCE,
			generator = "DefaultAddressSeq"
		)
	private Long addressid;
	
	//회원아이디
	@Column
	private String userid;
	//배송지이름
	@Column(nullable = false)
	private String name;
	//전화번호
	@Column(nullable = false)
    private String phone;
    //배송지
	@Column(nullable = false,  length = 2000)
    private String address;
	@Column
	private boolean isDefault; // 기본 배송지 여부
	
}
