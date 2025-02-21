package com.example.Modeme.Mypage.MypageDTO;

import lombok.Data;

@Data
public class WishlistRequest {
    private Long itemNumber;
    private int quantity; // 수량 필드 추가
    
}
