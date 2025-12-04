package com.example.app.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class RentalRecord {

	private Integer id;
	private Integer employeeId;
	private Integer toolId;
	private Integer shippingId;
	private LocalDateTime borrowingRequestedAt;
	private LocalDateTime borrowedAt;
	private LocalDateTime returnedAt;

	// 入出庫履歴表示用の従業員名
	private String employeeName;

}
