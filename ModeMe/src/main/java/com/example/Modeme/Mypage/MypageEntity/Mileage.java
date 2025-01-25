package com.example.Modeme.Mypage.MypageEntity;

import java.time.LocalDateTime;

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
@Table(name="Mileage")
@SequenceGenerator(
		allocationSize = 1,
		initialValue = 1,
		name = "MileageSeq",
		sequenceName = "MileageSeq"
	)
public class Mileage {
	@Id
	@GeneratedValue(
			strategy = GenerationType.SEQUENCE,
			generator = "MileageSeq"
		)
	private Long mileageid;
	
	//사용자 아이디
	@Column
	private String userid;
	
	//마일리지 적립일
	@Column
	private LocalDateTime createAt;
	
	//마일리지 적립금
	@Column
	private int mileage = 2000;
	
	//주문번호
	@Column
	private String ordernum;
	
	//마일리지 적립 내용
	@Column
	private String content;
}
