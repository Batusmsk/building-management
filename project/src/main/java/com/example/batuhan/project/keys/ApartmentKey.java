package com.example.batuhan.project.keys;

import java.io.Serializable;


import lombok.Data;
@Data
public class ApartmentKey implements Serializable{
	private String block;
	private Integer apartmentNo;
}
