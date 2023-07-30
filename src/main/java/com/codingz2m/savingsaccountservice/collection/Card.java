package com.codingz2m.savingsaccountservice.collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="card")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class Card {
	@Id
	private String cardId;
	private String cardType;
	private String cardTitle;
	private double annualFee;
	private String introductoryOffer;
	private boolean theftProtection;
}
