package com.vti.service;

import java.util.Collections;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.vti.entity.Account;
import com.vti.form.account.AccountFilterForm;
import com.vti.form.account.CreatingAccountForm;
import com.vti.repository.IAccountRepository;
import com.vti.specification.account.AccountSpecification;

@Service
public class AccountService implements IAccountService {

	@Autowired
	private IAccountRepository repository;

	@Autowired
	private ModelMapper modelMapper;
	
	public Page<Account> getAllAccounts(
			Pageable pageable, 
			String search, 
			AccountFilterForm filterForm) {
		
		Specification<Account> where = AccountSpecification.buildWhere(search, filterForm);
		return repository.findAll(where, pageable);
	}
	
	public Account getAccountByID(int id) {
		return repository.findById(id).get();
	}
	
	public void createAccount(CreatingAccountForm form) {
		
		// omit id field
		TypeMap<CreatingAccountForm, Account> typeMap = modelMapper.getTypeMap(CreatingAccountForm.class, Account.class);
		if (typeMap == null) { // if not already added
			// skip field
			modelMapper.addMappings(new PropertyMap<CreatingAccountForm, Account>() {
				@Override
				protected void configure() {
					skip(destination.getId());
				}
			});
		}

		// convert form to entity
		Account account = modelMapper.map(form, Account.class);
		
		repository.save(account);
	}

	public boolean isAccountExistsByUsername(String username) {
		return repository.existsByUsername(username);
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Account account = repository.findByUsername(username);

		if (account == null) {
			throw new UsernameNotFoundException(username);
		}
		
		return new User(
				account.getUsername(), 
				account.getPassword(), 
				Collections.emptyList());
	}
	
	@Override
	public Account getAccountByUsername(String username) {
		return repository.findByUsername(username);
	}

}


