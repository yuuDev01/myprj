package com.example.myprj.web;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.myprj.domain.common.mail.MailService;
import com.example.myprj.domain.common.util.PasswordGenerator;
import com.example.myprj.domain.common.util.PasswordGeneratorCreator;
import com.example.myprj.domain.member.svc.MemberSVC;
import com.example.myprj.web.api.FindEmailReq;
import com.example.myprj.web.form.member.FindPwForm;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/help")
@AllArgsConstructor
public class HelpController {
	private final MemberSVC memberSVC;
	private final MailService ms;
	
	@GetMapping("/")
	public String help() {
		return "help/help";
	}
	
	// 회원 아이디 찾기
	@GetMapping("/findId")
	public String findId(Model model) {
		
		model.addAttribute("findEmailReq", new FindEmailReq());
		return "help/findIdForm";
	}
	
	// 회원 비밀번호 찾기양식
	@GetMapping("/findPw")
	public String findPwForm(Model model) {
		model.addAttribute("findPwForm", new FindPwForm());
		return "help/findPwForm";
	}
	
	// 회원 비밀번호 찾기양식
	@PutMapping("/findPw")
	public String findPw(
			@Valid @ModelAttribute FindPwForm findPwForm,
			BindingResult bindingResult, HttpServletRequest request,
			Model model) {
		
		if(bindingResult.hasErrors()) {
			return "help/findPwForm";
		}
		
		
		String findedPw = memberSVC.findPw(
				findPwForm.getEmail(), findPwForm.getTel(), findPwForm.getBirth());
		
//		if(findedPw != null) {
//			
//		}
		
		//1)임의의 비밀번호 생성
		String tmpPw = PasswordGeneratorCreator.generator(7);
		
		//2)임시비밀번호로 회원의 비밀번호 변경
		memberSVC.changePw(findPwForm.getEmail(), findedPw, tmpPw);
		
		//3)생성된 비밀번호 이메일 전송
		String subject = "신규 비밀번호 전송";
		
		//로긴주소
		StringBuilder url = new StringBuilder();
		url.append("http://" + request.getServerName());
		url.append(":" + request.getServerPort());
		url.append(request.getContextPath());
		url.append("/login");
		
		//메일본문내용
		StringBuilder sb = new StringBuilder();
		sb.append("<!DOCTYPE html>");
		sb.append("<html lang='ko'>");
		sb.append("<head>");
		sb.append("  <meta charset='UTF-8'>");
		sb.append("  <meta name='viewport' content='width=device-width, initial-scale=1.0'>");
		sb.append("  <title>임시 비밀번호 발송</title>");
		sb.append("</head>");
		sb.append("<body>");
		sb.append("  <h1>신규비밀번호</h1>");
		sb.append("  <p>아래 비밀번호로 로그인 하셔서 비밀번호를 변경하세요</p>");
		sb.append("  <p>비밀번호 :" + tmpPw + "</p>");
		sb.append("  <a href='"+ url +"'>로그인</a>");
		sb.append("</body>");
		sb.append("</html>");
		
		ms.sendMail(findPwForm.getEmail(), subject , sb.toString());
		
		model.addAttribute("info", "가입된 이메일로 임시비밀번호가 발송되었습니다.");
		
		return "help/findPwForm";
	}
}