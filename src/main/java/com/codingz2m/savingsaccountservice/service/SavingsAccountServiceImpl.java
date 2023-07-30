package com.codingz2m.savingsaccountservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import com.codingz2m.savingsaccountservice.collection.SavingsAccount;
import com.codingz2m.savingsaccountservice.dto.SavingsAccountDto;
import com.codingz2m.savingsaccountservice.exception.SavingsAccountNotFoundException;
import com.codingz2m.savingsaccountservice.repository.SavingsAccountRepository;


@Service
public class SavingsAccountServiceImpl implements SavingsAccountService {

	@Autowired
	private SavingsAccountRepository savingsAccountRepository;
	@Autowired
	private MongoTemplate mongoTemplate;
	
	  @Autowired
	  ModelMapper modelMapper;
	  
	@Override
	public SavingsAccount save(SavingsAccountDto savingsAccountDto) {
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		SavingsAccount  savingsAccount = modelMapper.map( savingsAccountDto, SavingsAccount.class); 
		System.out.println(savingsAccount.getAccountType());
		return savingsAccountRepository.save( savingsAccount);
	}

	

	@Override
	public List<SavingsAccount> getSavingsAccountStartsWith(String holderName) throws SavingsAccountNotFoundException {
		
		List<SavingsAccount> savingsAccounts = savingsAccountRepository.findByHolderNameStartsWith(holderName);
		if ( savingsAccounts.isEmpty() ) {
			throw new SavingsAccountNotFoundException("Savings Accounts Are Not Found for the Account Holder Names " + " starts with " + holderName);
		}
		else {
			return savingsAccounts;
		}
			
		
	}
	
	@Override
	public SavingsAccount getSavingsAccount(String savingsAccountId) throws SavingsAccountNotFoundException {
		Optional<SavingsAccount> optionalSavingsAccount = savingsAccountRepository.findById(savingsAccountId);
		if ( !optionalSavingsAccount.isPresent() ) {
			throw new SavingsAccountNotFoundException("SavingsAccount Not Found for the ID: " + savingsAccountId);
		}
		else {
			return optionalSavingsAccount.get();
		}
	}

	
	
	@Override
	public void deleteSavingsAccount(String savingsAccountId) throws SavingsAccountNotFoundException {
		Optional<SavingsAccount> optionalSavingsAccount = savingsAccountRepository.findById(savingsAccountId);
		if ( !optionalSavingsAccount.isPresent() ) {
			throw new SavingsAccountNotFoundException("SavingsAccount ID: " + savingsAccountId + " Not Found to Delete");
		}
		else {
		savingsAccountRepository.deleteById(savingsAccountId);
		}
	}
	
	@Override
	public List<SavingsAccount> getAllSavingsAccounts() {
		return savingsAccountRepository.findAll();
	}
	
	@Override
	public List<SavingsAccount> savingsAccountService(double minCurrentValue, double maxCurrentValue) {
		// TODO Auto-generated method stub
		return savingsAccountRepository.findSavingsAccountsByCurrentValueBetween(minCurrentValue, maxCurrentValue);
	}


	@Override
	public Page<SavingsAccount> search(String name, Double minCurrentValue, Double maxCurrentValue, Pageable pageable) {

		Query query = new Query().with(pageable);
		List<Criteria> criteria = new ArrayList<>();
		
		 if(name !=null && !name.isEmpty()) {
		// We can fetch the records from MongoDB that matches the regex pattern mentioned in the queries.
			 criteria.add(Criteria.where("holderName").regex(name, "i"));
		 }
		 if(minCurrentValue !=null  && maxCurrentValue !=null) {
					 criteria.add(Criteria.where("currentValue").gte(minCurrentValue).lte(maxCurrentValue));
		 }
		 
		 if(!criteria.isEmpty()) {
			 query.addCriteria(new Criteria()
					 .andOperator(criteria.toArray(new Criteria[0])));
		 }
		 
		 Page<SavingsAccount> savingsAccounts = PageableExecutionUtils.getPage(
				 mongoTemplate.find(query, SavingsAccount.class), pageable, 
				 ()-> mongoTemplate.count(query.skip(0).limit(0), SavingsAccount.class));
		 
		return savingsAccounts;
	}


	 // Obtaining Higher Current Value Savings Accounts Group by Card Type
	@Override
	public List<Document> getHigherCurrentValueSavingsAccount() {
		UnwindOperation UnwindOperation
					= Aggregation.unwind("cards");
		SortOperation sortOperation = Aggregation.sort(Sort.Direction.DESC, "currentValue");
		GroupOperation groupOperation 
				=  Aggregation.group("cards.cardType")
				.first(Aggregation.ROOT)
				.as("higherCurrentValueSavingsAccount");
		
		Aggregation  aggregation 
					= 	Aggregation.newAggregation(UnwindOperation, sortOperation, groupOperation );

		List<Document> savingsAccounts
								= mongoTemplate.aggregate(aggregation, SavingsAccount.class, Document.class).getMappedResults(); 
		
		return savingsAccounts;
	}

   

	   // Obtaining Balances by Current Value
	@Override
	public List<Document> getBalanceByCurrentValue() {
	//	UnwindOperation UnwindOperation = Aggregation.unwind("currentValue");
		GroupOperation groupOperation 
							=  Aggregation.group("currentValue")
							  .count().as("higherCurrentValueSavingsAccountsCount");
		
		SortOperation sortOperation = Aggregation.sort(Sort.Direction.DESC, "higherCurrentValueSavingsAccountsCount");
		
		ProjectionOperation projectionOperation 
		= 	Aggregation.project()
			.andExpression("_id").as("Current Value")
			.andExpression("higherCurrentValueSavingsAccountsCount").as("Count")
			.andExclude("_id");
		
		Aggregation  aggregation = 
				 	Aggregation.newAggregation(groupOperation, sortOperation, projectionOperation );

		List<Document> savingsAccounts
					= mongoTemplate.aggregate(aggregation, SavingsAccount.class, Document.class)
					  .getMappedResults();
		
		return savingsAccounts;
	}

}