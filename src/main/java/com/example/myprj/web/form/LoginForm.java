package com.example.myprj.web.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class LoginForm {
	@NotBlank
	@Email
	@Size(min = 4,max = 40)
	private String email;
	
	@NotBlank
	@Size(min = 4,max = 12)
	private String pw;
}
