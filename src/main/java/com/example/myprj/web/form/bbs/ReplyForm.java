package com.example.myprj.web.form.bbs;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class ReplyForm {
	@NotBlank
	private String bcategory;
	@NotBlank
	private String btitle;
	@NotBlank
	private String bnickname;
	@NotBlank
	private String bcontent;

}
