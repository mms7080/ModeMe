package com.example.Modeme.prdDetail.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Modeme.prdDetail.repository.ProductDetailRepository;

@Service
public class ProductEditService {
	
	@Autowired
	private final ProductDetailRepository detailRepository;
	
	public ProductEditService(ProductDetailRepository detailRepository) {
		this.detailRepository = detailRepository;
	}
	
	
}