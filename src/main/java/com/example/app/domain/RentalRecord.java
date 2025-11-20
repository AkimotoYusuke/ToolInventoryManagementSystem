package com.example.app.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class RentalRecord {

	private Integer id;
	private Integer employeeId;
	private Integer toolId;
	private LocalDateTime borrowedAt;
	private LocalDateTime returnedAt;

	// 入出庫記録表示用
	private String employeeName;

}
