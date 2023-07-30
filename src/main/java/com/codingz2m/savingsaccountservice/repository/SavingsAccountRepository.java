package com.codingz2m.savingsaccountservice.repository;

import java.util.List;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import org.springframework.stereotype.Repository;
import com.codingz2m.savingsaccountservice.collection.SavingsAccount;

@Repository
public interface SavingsAccountRepository extends MongoRepository<SavingsAccount, String> {
	
	List<SavingsAccount> findByHolderNameStartsWith(String holderName);
	
	
	@Query( value = "{'currentValue' : { $gt : ?0, $lt : ?1}}",
			fields = "{contact:1}" )
	List<SavingsAccount> findSavingsAccountsByCurrentValueBetween(double minCurrentValue, double maxCurrentValue);
	

}

