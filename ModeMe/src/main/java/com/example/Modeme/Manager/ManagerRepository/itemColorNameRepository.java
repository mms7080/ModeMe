package com.example.Modeme.Manager.ManagerRepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Modeme.Manager.Entity.ItemColorName;

@Repository
public interface itemColorNameRepository extends JpaRepository<ItemColorName, Long>{
	Optional<ItemColorName> findById(Long id); // 색상 ID로 색상명 찾기
}
