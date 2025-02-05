package com.example.Modeme.Manager.ManagerRepository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Modeme.Manager.Entity.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    List<ProductImage> findByAddItemId(Long addItemId); // 특정 상품의 이미지 찾기
}
