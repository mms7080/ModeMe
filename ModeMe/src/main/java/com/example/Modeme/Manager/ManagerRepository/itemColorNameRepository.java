package com.example.Modeme.Manager.ManagerRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Modeme.Manager.Entity.ItemColorName;

@Repository
public interface itemColorNameRepository extends JpaRepository<ItemColorName, Long>{
	
}
