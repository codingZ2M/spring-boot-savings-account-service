package com.codingz2m.savingsaccountservice.collection;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="savingsaccount")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class SavingsAccount {
    @Id
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
