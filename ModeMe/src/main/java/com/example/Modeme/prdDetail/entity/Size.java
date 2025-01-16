package com.example.Modeme.prdDetail.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "SIZE_TABLE")
@SequenceGenerator(name = "SizeSeq", sequenceName = "SizeSeq", allocationSize = 1, initialValue = 1)
public class Size {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SizeSeq")
	@Column(name = "size_id")
	private Long id;

	@Column(name = "free_size")
	private String freeSize;

	@Column(name = "s")
	private String s;

	@Column(name = "m")
	private String m;

	@Column(name = "l")
	private String l;

	@Column(name = "xl")
	private String xl;
}
