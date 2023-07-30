package com.codingz2m.savingsaccountservice.collection;


import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="contact")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class Contact {

	 private String phone;
	 private String email;
	 private String suite;
	 private String city;
	 private String state;
	 private String zipCode;
	 
}
