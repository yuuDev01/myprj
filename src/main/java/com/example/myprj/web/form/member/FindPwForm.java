package com.example.myprj.web.form.member;

import java.sql.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class FindPwForm {
	
	@NotBlank
	@Email
	private String email;
	
	@NotBlank
	@Size(max = 13)
	private String tel;
	
	private Date birth;
}
