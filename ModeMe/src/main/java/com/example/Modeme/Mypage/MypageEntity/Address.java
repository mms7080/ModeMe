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
@Table(name = "Address")
@SequenceGenerator(
		allocationSize = 1,
		initialValue = 1,
		name = "ReservationSeq",
		sequenceName = "ReservationSeq"
	)
public class Address {
	@Id
	@GeneratedValue(
			strategy = GenerationType.SEQUENCE,
			generator = "ReservationSeq"
		)
	private Long addressid;
	
	//이름
	@Column(nullable = false)
	private String name;
	//전화번호
	@Column(nullable = false)
    private int phone;
    //배송지
	@Column(nullable = false,  length = 2000)
    private String address;
}

