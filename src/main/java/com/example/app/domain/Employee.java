package com.example.app.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class Employee {

	private Integer id;

	@NotBlank
	@Size(max = 30)
	private String name;

	@NotBlank
	@Size(max = 30)
	private String loginId;

	@NotBlank
	@Size(max = 60)
	private String loginPass;

	private String status;
	
	private AuthorityType authorityType;

}
