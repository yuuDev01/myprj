package com.example.myprj.web.form.member;

import java.sql.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import com.example.myprj.domain.common.dto.UpLoadFileDTO;

import lombok.Data;

@Data
public class EditForm {
	private String email;						//아이디(이메일)
	@NotBlank
	@Size(min = 4,max = 10)
	private String pw;							//비밀번호	
	private String tel;							//연락처
	private String nickname;				//별칭
	private String gender;					//성별
	private String region;					//지역
	private Date birth;							//생년월일
	private List<String> hobby;			//취미
	private boolean letter;					//뉴스레터가입유무
	
	private MultipartFile imgFile; 		//신규파일	
	private UpLoadFileDTO savedImgFile; 		//기존파일
}
