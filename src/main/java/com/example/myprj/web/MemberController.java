package com.example.myprj.web;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.myprj.domain.common.dao.CodeDAO;
import com.example.myprj.domain.member.dto.MemberDTO;
import com.example.myprj.domain.member.svc.MemberSVC;
import com.example.myprj.web.form.Code;
import com.example.myprj.web.form.member.JoinForm;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/members")
public class MemberController {

	private final MemberSVC memberSVC;
	private final CodeDAO codeDAO;
	
	@ModelAttribute("hobby")
	public List<Code> hobby(){
		List<Code> list = codeDAO.getCode("A01");
		log.info("code-hobby:{}",list);
		return list;
	}
	@ModelAttribute("gender")
	public List<Code> gender(){
		List<Code> list = codeDAO.getCode("A02");
		log.info("code-gender:{}",list);
		return list;
	}
	@ModelAttribute("region")
	public List<Code> region(){
		List<Code> list = codeDAO.getCode("A03");
		log.info("code-region:{}",list);
		return list;
	}
	/**
	 * 회원가입양식
	 * @return
	 */
	@GetMapping("/join")
	public String joinForm(Model model) {
		log.info("회원가입양식 호출됨!");
		model.addAttribute("joinForm", new JoinForm());
		return "members/joinForm";
	}
	/**
	 * 회원가입처리
	 * @return
	 */
	@PostMapping("/join")
	public String join(
			@Valid @ModelAttribute JoinForm joinForm,
			BindingResult bindingResult) {
		log.info("회원가입처리 호출됨!");
		log.info("joinForm:{}",joinForm);
		
		
		//비밀번호 확인 체크
		if(!joinForm.getPw().equals(joinForm.getPwchk())) {
			bindingResult.reject("error.member.join", "비밀번호가 다릅니다.");
			return "members/joinForm";			
		}
		
		//회원 존재유무
		if(memberSVC.isExistEmail(joinForm.getEmail())) {
			bindingResult.reject("error.member.join", "동일한 이메일이 존재합니다");
			return "members/joinForm";
		}
		
		if(bindingResult.hasErrors()) {
			log.info("errors={}",bindingResult);
			return "members/joinForm";
		}
		
		MemberDTO mdto = new MemberDTO();
		BeanUtils.copyProperties(joinForm, mdto, "letter" );
		mdto.setLetter(joinForm.isLetter() ? "1" : "0");
		memberSVC.join(mdto);
		
		return "redirect:/login";
	}
	/**
	 * 회원수정양식
	 * @return
	 */
	@GetMapping("/{id:.+}/edit")
	public String editForm(@PathVariable("id") String id) {
		log.info("회원수정양식 호출됨!");
		log.info("회원:{}",id);
		return "members/editForm";
	}
	/**
	 * 회원수정처리
	 * @return
	 */
	@PatchMapping("/{id:.+}/edit")
	public String edit(@PathVariable("id") String id) {
		log.info("회원수정처리 호출됨");
		log.info("회원:{}",id);
		return "home";
	}
	/**
	 * 회원조회
	 * @return
	 */
	@GetMapping("/{id:.+}")
	public String view(@PathVariable("id") String id) {
		log.info("회원조회 호출됨");
		log.info("회원:{}",id);
		return "members/view";
	}
	/**
	 * 회원탈퇴
	 * @return
	 */
	@DeleteMapping("/{id:.+}")
	public String out(@PathVariable("id") String id) {
		log.info("회원탈퇴");
		log.info("회원:{}",id);
		return "home";
	}
	/**
	 * 회원목록
	 * @return
	 */
	@GetMapping("")
	public String listAll() {
		log.info("회원목록");
		return "members/list";
	}
}
