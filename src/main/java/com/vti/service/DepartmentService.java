package com.vti.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.vti.entity.Account;
import com.vti.entity.Department;
import com.vti.form.department.CreatingDepartmentForm;
import com.vti.form.department.DepartmentFilterForm;
import com.vti.form.department.UpdatingDepartmentForm;
import com.vti.repository.IAccountRepository;
import com.vti.repository.IDepartmentRepository;
import com.vti.specification.department.DepartmentSpecification;

@Service
public class DepartmentService implements IDepartmentService {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private IDepartmentRepository repository;
	
	@Autowired
	private IAccountRepository accountRepository;
	
	public Page<Department> getAllDepartments(Pageable pageable, String search, DepartmentFilterForm filterForm) {

		Specification<Department> where = DepartmentSpecification.buildWhere(search, filterForm);
		return repository.findAll(where, pageable);
	}

	public Department getDepartmentByID(int id) {
		return repository.findById(id).get();
	}

	public void createDepartment(CreatingDepartmentForm form) {

		// convert form to entity
		Department departmentEntity = modelMapper.map(form, Department.class);

		// create department
		Department department = repository.save(departmentEntity);
		
		// create accounts
		List<Account> accountEntities = departmentEntity.getAccounts();
		for (Account account : accountEntities) {
			account.setDepartment(department);
		}
		accountRepository.saveAll(accountEntities);
	}

	public void updateDepartment(UpdatingDepartmentForm form) {

		// convert form to entity
		Department department = modelMapper.map(form, Department.class);

		repository.save(department);
	}

	public boolean isDepartmentExistsByName(String name) {
		return repository.existsByName(name);
	}

	public boolean isDepartmentExistsByID(Integer id) {
		return repository.existsById(id);
	}

}
