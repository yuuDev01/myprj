package com.example.myprj.web.form.bbs;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class WriteForm {
	@NotBlank
	private String bcategory;
	@NotBlank
	private String btitle;
	@NotBlank
	private Long bid;
	@NotBlank
	private String bemail;
	@NotBlank
	private String bnickname;
	@NotBlank
	private String bcontent;

}
