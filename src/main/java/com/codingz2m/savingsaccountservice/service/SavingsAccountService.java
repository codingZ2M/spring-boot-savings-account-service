package com.codingz2m.savingsaccountservice.service;

import java.util.List;

import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.codingz2m.savingsaccountservice.collection.SavingsAccount;
import com.codingz2m.savingsaccountservice.dto.SavingsAccountDto;
import com.codingz2m.savingsaccountservice.exception.SavingsAccountNotFoundException;


public interface SavingsAccountService {

	SavingsAccount save(SavingsAccountDto savingsAccountDto);

	List<SavingsAccount> getSavingsAccountStartsWith(String holderName) throws SavingsAccountNotFoundException;

	void deleteSavingsAccount(String savingsAccountId) throws SavingsAccountNotFoundException;

	List<SavingsAccount> getAllSavingsAccounts();
	
	List<SavingsAccount> savingsAccountService(double minCurrentValue, double maxCurrentValue);

	Page<SavingsAccount> search(String name, Double minCrrentValue, Double maxCrrentValue, Pageable pageable);

	SavingsAccount getSavingsAccount(String savingsAccountId) throws SavingsAccountNotFoundException;

	List<Document> getHigherCurrentValueSavingsAccount();

	List<Document> getBalanceByCurrentValue();

	

	



}
