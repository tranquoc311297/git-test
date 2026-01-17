package com.vti.form.department;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdatingDepartmentForm {

	private int id;
	
	private String name;

	private int totalMember;

	private String type;

}
