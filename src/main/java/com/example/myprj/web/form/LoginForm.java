package com.example.myprj.web.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class LoginForm {
	@NotBlank(message="공백문자(x)")
	@Email
	@Size(min = 4 , max =40)
	private String email;
	
	@NotBlank(message="공백문자(x)")
	@Size(min = 4, max = 8)
	private String pw;
}
