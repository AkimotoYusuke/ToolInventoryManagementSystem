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
	
	private int id;
	
	@NotBlank
	@Size(max = 30)
	private String recipientName;
	
	@NotNull
	@Size(max = 100)
	private String recipientAddress;
	
	@NotNull
	@Size(max = 20)
	private String recipientPhone;
	
	@NotNull
	@Size(max = 30)
	private String senderName;
	
	@NotNull
	@Size(max = 100)
	private String senderAddress;
	
	@NotNull
	@Size(max = 20)
	private String senderPhone;
	
	@NotNull
	@Size(max = 30)
	private String shippingMethod;
	
	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate shipDate;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate arrivalDate;
	
	@Size(max = 255)
	private String note;
	
	@NotBlank
	@Size(max = 10)
	private String shippingNumber;
	
	private Integer rentalRecordId;
	private LocalDateTime ShipmentRequestAt;
	private LocalDateTime ShipmentAt;

}
