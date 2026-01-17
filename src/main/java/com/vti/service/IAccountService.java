package com.vti.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.vti.entity.Account;
import com.vti.form.account.AccountFilterForm;
import com.vti.form.account.CreatingAccountForm;

public interface IAccountService extends UserDetailsService{

	public Page<Account> getAllAccounts(Pageable pageable, String search, AccountFilterForm filterForm);

	public Account getAccountByID(int id);
	
	public void createAccount(CreatingAccountForm form);
	
	public boolean isAccountExistsByUsername(String username);
	
	public Account getAccountByUsername(String username);
}
