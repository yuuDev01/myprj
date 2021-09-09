package com.example.myprj.web.form.bbs;

import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.web.multipart.MultipartFile;

import com.sun.istack.NotNull;

import lombok.Data;

@Data
public class EditForm {
	private Long 	 pbnum;				//부모글 번호
	private Long 	 bnum;				//글 번호
	@NotBlank
	private String bcategory;
	@NotNull
	private Long bid;		
	@NotBlank
	private String bemail;	
	@NotBlank
	private String btitle;
	@NotBlank
	private String bnickname;
	@NotBlank
	private String bcontent;
	
	private List<MultipartFile> files; //파일첨부	
}
