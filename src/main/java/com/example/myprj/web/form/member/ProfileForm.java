package com.example.myprj.web.form.member;

import com.example.myprj.domain.common.dto.UpLoadFileDTO;

import lombok.Data;

@Data
public class ProfileForm {
	private String nickname;								//별칭
	private UpLoadFileDTO savedImgFile; 		//기존파일
}