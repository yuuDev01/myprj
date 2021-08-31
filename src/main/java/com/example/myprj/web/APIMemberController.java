package com.example.myprj.web;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.myprj.domain.common.mail.MailService;
import com.example.myprj.domain.member.svc.MemberSVC;
import com.example.myprj.web.api.FindEmailReq;
import com.example.myprj.web.api.JsonResult;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController  //@Controller + @ResponseBody
@RequestMapping("/api/members")
@AllArgsConstructor
public class APIMemberController {

	private final MemberSVC memberSVC;
	private final MailService mailService;
//	private final PasswordGeneratorCreator passwordGeneratorCreator;
	
	//아이디(이메일) 중복체크
	@GetMapping("/email/dupchk")
	public JsonResult<String> dupChkEmail(
			@RequestParam String email
			){
		
		JsonResult<String> result = null;
		if(memberSVC.isExistEmail(email)) {
			result = new JsonResult<String>("00", "OK", email);
		}else {
			result = new JsonResult<String>("01", "NOK", null);
		}
		
		return result;
	}
	
	//이메일 찾기
	@PostMapping("/email")
	public JsonResult<String> findEamil(
			@RequestBody FindEmailReq findEmailReq,
			BindingResult bindingResult) {

		log.info("findEmailReq:{}",findEmailReq);
		if(bindingResult.hasErrors()) {
			return null;
		}
		
		String findedEmail = 
				memberSVC.findEmail(findEmailReq.getTel(),findEmailReq.getBirth());
		
		return new JsonResult<String>("00","ok",findedEmail);
	}

}
