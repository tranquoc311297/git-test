package com.vti.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vti.dto.AccountDTO;
import com.vti.entity.Account;
import com.vti.form.account.AccountFilterForm;
import com.vti.form.account.CreatingAccountForm;
import com.vti.service.IAccountService;

@RestController
@RequestMapping(value = "api/v1/accounts")
public class AccountController {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private IAccountService service;

	@GetMapping()
	public Page<AccountDTO> getAllAccounts(
			Pageable pageable, 
			@RequestParam(value = "search", required = false) String search,
			AccountFilterForm filterForm) {

		Page<Account> entityPages = service.getAllAccounts(pageable, search, filterForm);

		// convert entities --> dtos
		List<AccountDTO> dtos = modelMapper.map(
				entityPages.getContent(), 
				new TypeToken<List<AccountDTO>>() {}.getType());

		Page<AccountDTO> dtoPages = new PageImpl<>(dtos, pageable, entityPages.getTotalElements());

		return dtoPages;
	}
	
	@GetMapping(value = "/{id}")
	public AccountDTO getAccountByID(@PathVariable(name = "id") int id) {
		Account entity = service.getAccountByID(id);

		// convert entity to dto
		AccountDTO dto = modelMapper.map(entity, AccountDTO.class);
		
		dto.add(linkTo(methodOn(AccountController.class).getAccountByID(id)).withSelfRel());

		return dto;
	}

	@PostMapping()
	public void createAccount(@RequestBody CreatingAccountForm form) {
		service.createAccount(form);
	}
}

