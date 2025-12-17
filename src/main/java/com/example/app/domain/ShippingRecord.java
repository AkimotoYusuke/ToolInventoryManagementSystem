package com.example.app.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class ShippingRecord {
	
	private Integer id;
	
	@NotBlank
	@Size(max = 30)
	private String recipientName;
	
	@NotBlank
	@Size(max = 8)
	private String recipientPostCode;
	
	@NotBlank
	@Size(max = 100)
	private String recipientAddress;
	
	@NotBlank
	@Size(max = 13)
	private String recipientPhone;
	
	@NotBlank
	@Size(max = 30)
	private String senderName;
	
	@NotBlank
	@Size(max = 8)
	private String senderPostCode;
	
	@NotBlank
	@Size(max = 100)
	private String senderAddress;
	
	@NotBlank
	@Size(max = 13)
	private String senderPhone;
	
	@NotBlank
	@Size(max = 30)
	private String shippingMethod;
	
	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate shipDate;
	
	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate arrivalDate;
	
	@NotBlank
	@Size(max = 10)
	private String shippingNumber;
	
	@Size(max = 255)
	private String note;
	
	private Integer employeeId;
	private LocalDateTime shippingAddAt;
	private LocalDateTime shippingDeleteAt;
	private LocalDateTime shippingRequestedAt;
	private LocalDateTime shippedAt;
	private LocalDateTime returnedAt;
	private Integer unstockedTools;
	private Integer numberOfTools;
	
}
