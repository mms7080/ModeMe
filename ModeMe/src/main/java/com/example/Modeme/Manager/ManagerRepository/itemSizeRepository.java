package com.example.Modeme.Manager.ManagerRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Modeme.Manager.Entity.ItemSize;

@Repository
public interface itemSizeRepository extends JpaRepository<ItemSize, Long>{

}
