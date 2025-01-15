package com.example.Modeme.Mypage.MypageDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {
		private Long addressid;
		private String name;
		private int phone;
		private String address;
}
