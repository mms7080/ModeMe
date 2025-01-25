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
	
	@Column
	private String userid;
	
	@Column
	private LocalDateTime createAt;
	
	@Column
	private int mileage = 2000;
	
	@Column
	private String ordernum;
	
	@Column
	private String content;
}
