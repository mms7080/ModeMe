package com.example.Modeme.Mypage.MypageDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DefaultaddressDTO {
	private Long addressid;
	private String name;
	private String phone;
	private String address;
	private boolean isDefault;
}
