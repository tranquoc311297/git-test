package com.vti.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.Locale;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vti.dto.DepartmentDTO;
import com.vti.entity.Department;
import com.vti.form.department.CreatingDepartmentForm;
import com.vti.form.department.DepartmentFilterForm;
import com.vti.form.department.UpdatingDepartmentForm;
import com.vti.service.IDepartmentService;
import com.vti.validation.department.DepartmentIDExists;

@RestController
@RequestMapping(value = "api/v1/departments")
@Validated
public class DepartmentController {

	@Autowired
	private IDepartmentService service;

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private MessageSource messageSource;
	
	@GetMapping()
	public Page<DepartmentDTO> getAllDepartments(
			Pageable pageable, 
			@RequestParam(name = "search", required = false) String search,
			DepartmentFilterForm filterForm) {
		Page<Department> entityPages = service.getAllDepartments(pageable, search, filterForm);

		// convert entities --> dtos
		List<DepartmentDTO> dtos = modelMapper.map(
				entityPages.getContent(), 
				new TypeToken<List<DepartmentDTO>>() {}.getType());
		
		// add HATEOAS
		for (DepartmentDTO dto : dtos) {
			for (DepartmentDTO.AccountDTO accountDTO : dto.getAccounts()) {
				accountDTO.add(linkTo(methodOn(AccountController.class).getAccountByID(accountDTO.getId())).withSelfRel());
			}
			dto.add(linkTo(methodOn(DepartmentController.class).getDepartmentByID(dto.getId())).withSelfRel());
		}
	
		Page<DepartmentDTO> dtoPages = new PageImpl<>(dtos, pageable, entityPages.getTotalElements());

		return dtoPages;
	}
	
	@GetMapping(value = "/{id}")
	public DepartmentDTO getDepartmentByID(@PathVariable(name = "id") @DepartmentIDExists int id) {
		Department entity = service.getDepartmentByID(id);

		// convert entity to dto
		DepartmentDTO dto = modelMapper.map(entity, DepartmentDTO.class);
		
		dto.add(linkTo(methodOn(DepartmentController.class).getDepartmentByID(id)).withSelfRel());

		return dto;
	}

	@PostMapping()
	public void createDepartment(@RequestBody @Valid CreatingDepartmentForm form) {
		service.createDepartment(form);
	}
	
	@PutMapping(value = "/{id}")
	public void updateDepartment(
			@DepartmentIDExists @PathVariable(name = "id") int id, 
			@RequestBody UpdatingDepartmentForm form) {
		form.setId(id);
		service.updateDepartment(form);
	}

	@GetMapping("/messages")
	public String testMessages(@RequestParam(value = "key") String key){
		return messageSource.getMessage(
				key, 
				null, 
				"Default message", 
				LocaleContextHolder.getLocale());
	}
	
	@GetMapping("/messages/vi")
	public String testMessagesVi(@RequestParam(value = "key") String key){
		return messageSource.getMessage(
				key, 
				null, 
				"Default message", 
				new Locale("vi", "VN"));
	}
	
	@GetMapping("/messages/en")
	public String testMessagesOther(@RequestParam(value = "key") String key){
		return messageSource.getMessage(
				key, 
				null, 
				"Default message", 
				Locale.US);
	}

	@GetMapping("/exception")
	public void testException() throws Exception {
		// ... other logic
		throw new EntityNotFoundException("... Exception Information");
		// ... other code
	}
}


