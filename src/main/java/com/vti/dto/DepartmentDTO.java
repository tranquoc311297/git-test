package com.vti.dto;

import java.util.Date;
import java.util.List;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DepartmentDTO extends RepresentationModel<DepartmentDTO> {

	private int id;

	private String name;

	private String type;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date createdDate;

	private List<AccountDTO> accounts;

	@Data
	@NoArgsConstructor
	public static class AccountDTO extends RepresentationModel<DepartmentDTO> {

		@JsonProperty("accountId")
		private int id;

		private String username;
	}
}

