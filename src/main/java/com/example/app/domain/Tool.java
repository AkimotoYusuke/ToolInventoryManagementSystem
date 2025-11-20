package com.example.app.domain;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class Tool {

	private Integer id;
	
	@NotBlank
	@Size(max = 10)
	private String mgmtId;

	@NotBlank
	@Size(max = 50)
	private String name;

	@Size(max = 100)
	private String detailInfo;

	private MakerType makerType;
	private LocalDateTime created;
	private String status;

	private Boolean reserved;
	private int reservedEmployeeId;
	private Integer rentalId;
	
}
