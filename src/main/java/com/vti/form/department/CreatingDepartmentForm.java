package com.vti.form.department;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;

import org.hibernate.validator.constraints.Length;

import com.vti.validation.account.AccountUsernameNotExists;
import com.vti.validation.department.DepartmentNameNotExists;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreatingDepartmentForm {

	@NotBlank(message = "{Department.createDepartment.form.name.NotBlank}")
	@Length(max = 50, message = "{Department.createDepartment.form.name.Length}")
	@DepartmentNameNotExists
	private String name;

	@NotNull(message = "The Total Member mustn't be null value")
	@PositiveOrZero(message = "The Total Member must be greater than or equal 0")
	private Integer totalMember;

	@Pattern(regexp = "DEV|TEST|PM", message = "The type must be DEV, TEST or PM")
	private String type;
	
	@NotEmpty(message = "Accounts mustn't be empty")
	private List<@Valid Account> accounts;

	@Data
	@NoArgsConstructor
	public static class Account {
		
		@NotBlank(message = "The name mustn't be null value")
		@Length(max = 50, message = "The name's length is max 50 characters")
		@AccountUsernameNotExists
		private String username;
	}
}
