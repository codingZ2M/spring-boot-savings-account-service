package com.codingz2m.savingsaccountservice.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bson.Document;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codingz2m.savingsaccountservice.collection.SavingsAccount;
import com.codingz2m.savingsaccountservice.dto.SavingsAccountDto;
import com.codingz2m.savingsaccountservice.exception.SavingsAccountNotFoundException;
import com.codingz2m.savingsaccountservice.service.SavingsAccountService;


@RestController
@RequestMapping("/savingsaccount")
public class SavingsAccountController {

   @Autowired
   private SavingsAccountService savingsAccountService;
   private static final Logger logger = LoggerFactory.getLogger(SavingsAccountController.class);

   @Autowired
	ModelMapper modelMapper;
   
   @PostMapping
   public SavingsAccountDto save (@RequestBody SavingsAccountDto savingsAccountDto) {
	
	  SavingsAccount savingsAccount = savingsAccountService.save(savingsAccountDto);
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		  savingsAccountDto = modelMapper.map( savingsAccount, SavingsAccountDto.class); 
		
	  return savingsAccountDto;
   }
   
   
   @GetMapping("/{id}")
   public SavingsAccountDto getSavingsAccount(@PathVariable("id") String savingsAccountId) throws SavingsAccountNotFoundException {
	   logger.info("getSavingsAccount() inside SavingsAccountController is executed");
	 
	   SavingsAccount savingsAccount =  savingsAccountService.getSavingsAccount(savingsAccountId);
	   
	   modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
	   SavingsAccountDto savingsAccountDto = modelMapper.map( savingsAccount, SavingsAccountDto.class); 
	  return savingsAccountDto;
   }
   
   
   @GetMapping("/all")
   public List<SavingsAccountDto> getAllSavingsAccounts() {
	   logger.info("getAllSavingsAccounts() inside SavingsAccountController is executed");
	   List<SavingsAccountDto> savingsAccountsDto= new ArrayList<SavingsAccountDto>();
	 
	   List<SavingsAccount> savingsAccounts = savingsAccountService.getAllSavingsAccounts();
	   Iterator<SavingsAccount> iterator = savingsAccounts.iterator(); 
	   
	   modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
	   
	   while(iterator.hasNext()) {
		   SavingsAccount savingsAccount = (SavingsAccount) iterator.next();
		   SavingsAccountDto savingsAccountDto = modelMapper.map( savingsAccount, SavingsAccountDto.class); 
		  savingsAccountsDto.add(savingsAccountDto);
	   }
	   return savingsAccountsDto;
   }
   
   
   //REST End Point: localhost:8081/savingsaccount/byHolderName?holderName=jhon
   @GetMapping("/byHolderName")
   public List<SavingsAccountDto> getSavingsAccountStartsWith(@RequestParam("holderName") String holderName) throws SavingsAccountNotFoundException {
	   
	   logger.info("getSavingsAccountStartsWith() inside SavingsAccountController is executed");
	   List<SavingsAccountDto> savingsAccountsDto= new ArrayList<SavingsAccountDto>();
	 
	    List<SavingsAccount> savingsAccounts = savingsAccountService.getSavingsAccountStartsWith(holderName);
	   Iterator<SavingsAccount> iterator = savingsAccounts.iterator(); 
	   
	   modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
	   
	   while(iterator.hasNext()) {
		   SavingsAccount savingsAccount = (SavingsAccount) iterator.next();
		   SavingsAccountDto savingsAccountDto = modelMapper.map( savingsAccount, SavingsAccountDto.class); 
		  savingsAccountsDto.add(savingsAccountDto);
	   }
	   return savingsAccountsDto;
   }
   
   
   @DeleteMapping("/{id}")
   public void deleteSavingsAccount(@PathVariable("id") String savingsAccountId) throws SavingsAccountNotFoundException {
	   logger.info("deleteSavingsAccount() inside SavingsAccountController is executed");
	   savingsAccountService.deleteSavingsAccount(savingsAccountId);
   }
   
   
   // Obtaining Higher Current Value Savings Accounts Group by Card Type
   @GetMapping("/higherCurrentValueSavingsAccount")
   public List<Document> getHigherCurrentValueSavingsAccount() {
	   	return savingsAccountService.getHigherCurrentValueSavingsAccount();
   }
   
   
   // Obtaining Balances by Current Value
   @GetMapping("/balanceByCurrentValue")
   public List<Document> getBalanceByCurrentValue() {
	   return savingsAccountService.getBalanceByCurrentValue();
   }
   
   
   @GetMapping("/search")
   public Page<SavingsAccount> searchSavingsAccount(
		   					@RequestParam(required=false) String holderName,
		   					@RequestParam(required=false) Double minCurrentValue,
		   					@RequestParam(required=false) Double maxCurrentValue,
		   					@RequestParam(defaultValue="0") Integer page,
		   					@RequestParam(defaultValue="5") Integer size
		   					){
	   		Pageable pageable = PageRequest.of(page, size);
	   		
	   		return savingsAccountService.search(holderName, minCurrentValue, maxCurrentValue, pageable);
   }
   
   

   
   
   // REST End Point: localhost:8081/savingsaccount/currentvalue?minCurrentvalue=9000&maxCurrentvalue=14000
   @GetMapping("/currentvalue")
   public List<SavingsAccountDto> getBySavingsAccountCurrentValue(
		   						@RequestParam("minCurrentvalue") double minCurrentValue,
   								@RequestParam("maxCurrentvalue") double maxCurrentValue) {
	   
	   List<SavingsAccountDto> savingsAccountsDto= new ArrayList<SavingsAccountDto>();
	   List<SavingsAccount> savingsAccounts =  savingsAccountService.savingsAccountService(minCurrentValue, maxCurrentValue);
	   
	   Iterator<SavingsAccount> iterator = savingsAccounts.iterator(); 
	   
	   modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
	   
	   while(iterator.hasNext()) {
		   SavingsAccount savingsAccount = (SavingsAccount) iterator.next();
		   SavingsAccountDto savingsAccountDto = modelMapper.map( savingsAccount, SavingsAccountDto.class); 
		  savingsAccountsDto.add(savingsAccountDto);
	   }
	   return savingsAccountsDto;
   }
   
   

}
