package com.example.Modeme.Manager.ManagerRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Modeme.Manager.Entity.ItemColor;

@Repository
public interface itemColorRepository extends JpaRepository<ItemColor, Long>{

}
