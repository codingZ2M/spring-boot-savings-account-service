package com.codingz2m.savingsaccountservice.dto;

import java.util.List;

import com.codingz2m.savingsaccountservice.collection.Card;
import com.codingz2m.savingsaccountservice.collection.Contact;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SavingsAccountDto {

	private String savingsAccountId;
	private String holderName;
	private String accountType;
	private double annualPercentageYield;
	private double minimumBalanceToAvailAPY;
	private double minimumBalanceToOpenAccount;
	private double currentValue;
	private List<Card> cards;
	
	private Contact contact;
}
