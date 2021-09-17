package com.example.myprj.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.myprj.domain.common.dao.CodeDAO;
import com.example.myprj.domain.common.dao.UpLoadFileDAO;
import com.example.myprj.domain.common.dto.UpLoadFileDTO;
import com.example.myprj.domain.common.file.FileStore;
import com.example.myprj.domain.member.dto.MemberDTO;
import com.example.myprj.domain.member.svc.MemberSVC;
import com.example.myprj.web.api.JsonResult;
import com.example.myprj.web.form.Code;
import com.example.myprj.web.form.LoginMember;
import com.example.myprj.web.form.member.ChangePwForm;
import com.example.myprj.web.form.member.EditForm;
import com.example.myprj.web.form.member.JoinForm;
import com.example.myprj.web.form.member.ProfileForm;

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
	private final FileStore fileStore;	
	private final UpLoadFileDAO upLoadFileDAO;
	
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
	@GetMapping("/edit")
	public String editForm(
			HttpServletRequest request,
			Model model) {
		log.info("회원수정양식 호출됨!");
		HttpSession session = request.getSession(false);
		LoginMember loginMember 
			= (LoginMember)session.getAttribute("loginMember");
		
		//세션이 없으면 로그인 페이지로 이동
		if(loginMember == null) return "redirect:/login";
		
		//회원정보 가져오기
		MemberDTO memberDTO =  memberSVC.findByEmail(loginMember.getEmail());
		
		EditForm editForm = new EditForm();
		BeanUtils.copyProperties(memberDTO, editForm);
		editForm.setSavedImgFile(memberDTO.getFile());
		
		if(memberDTO.getLetter().equals("1")) {
			editForm.setLetter(true);
		} else {
			editForm.setLetter(false);
		}
		
		model.addAttribute("editForm", editForm);
		
		return "mypage/memberEditForm";
	}
	/**
	 * 회원수정처리
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	@PatchMapping("/edit")
	public String edit(
			@Valid @ModelAttribute EditForm editForm,
			BindingResult bindingResult,
			HttpServletRequest request) throws IllegalStateException, IOException {
		
		log.info("회원수정처리 호출됨:{}",editForm);
		HttpSession session = request.getSession(false);
		LoginMember loginMember 
			= (LoginMember)session.getAttribute("loginMember");
		log.info("회원 수정 처리:{}"+loginMember.toString());
		//세션이 없으면 로그인 페이지로 이동
		if(loginMember == null) return "redirect:/login";
		
		//비밀번호를 잘못입력했을경우
		if(!memberSVC.isMemember(loginMember.getEmail(), editForm.getPw())) {
			bindingResult.rejectValue("pw", "error.member.editForm", "비밀번호가 잘못입력되었습니다.");
		}
		
		if(bindingResult.hasErrors()) {
			log.info("errors={}",bindingResult);		
			return "mypage/memberEditForm";
		}
		
		MemberDTO mdto = new MemberDTO();		
		BeanUtils.copyProperties(editForm, mdto);
		mdto.setLetter(editForm.isLetter() ? "1" : "0");
		
		memberSVC.update(loginMember.getId(), mdto);
		log.info("=={},{}",loginMember.getId(), mdto);
		return "redirect:/members/edit";
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
	 * 회원 탈퇴
	 * @return
	 */
	@GetMapping("/out")
	public String outForm() {
		log.info("회원 탈퇴 양식 호출");
		
		return "mypage/memberOutForm";
	}
	/**
	 * 회원탈퇴
	 * @return
	 */
	@DeleteMapping("/out")
	public String out(
			@RequestParam String pw,
			HttpServletRequest request,
			Model model
			) {
		log.info("회원탈퇴");
		
		Map<String, String> errors = new HashMap<>();
		
		if(pw == null || pw.trim().length() == 0) {
			errors.put("pw", "비밀번호를 입력하세요");
			model.addAttribute("errors", errors);
			return "mypage/memberOutForm";
		}
		
		HttpSession session = request.getSession(false);
		if(session == null) return "redirect:/login";
		
		LoginMember loginMember = (LoginMember)session.getAttribute("loginMember");
		//회원존재유무확인
		if(memberSVC.isMemember(loginMember.getEmail(), pw)) {		
			//탈퇴
			memberSVC.outMember(loginMember.getEmail(), pw);
		}else {
			errors.put("global", "비밀번호가 잘못되었습니다!");
			model.addAttribute("errors", errors);
		}
		
		if(!errors.isEmpty()) {
			return "mypage/memberOutForm"; 
		}
		
		//세션제거
		session.invalidate();

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
	
	/**
	 * 비밀번호 변경 양식
	 * @return
	 */
	@GetMapping("/pw")
	public String changePwForm(Model model) {
		
		model.addAttribute("changePwForm", new ChangePwForm());
		
		return "mypage/changePwForm";
	}
	/**
	 * 비밀번호 변경 처리
	 * @param changePwForm
	 * @param bindingResult
	 * @param request
	 * @return
	 */
	@PatchMapping("/pw")
	public String changePw(
			@Valid @ModelAttribute ChangePwForm changePwForm,
			BindingResult bindingResult,
			HttpServletRequest request) {
		
		HttpSession session = request.getSession(false);
		if(session == null) return "redirect:/";
		
		//변경할 비밀번호체크
		if(!changePwForm.getPostpw().equals(changePwForm.getPostpwChk())) {
			bindingResult.reject("error.member.changePw", "변경할 비밀번호가 일치하지 않습니다");
		}
		//이전 비밀번호와 변경할 비밀번호가 동일한지 체크
		if(changePwForm.getPrepw().equals(changePwForm.getPostpw())) {
			bindingResult.reject("error.member.changePw", "이전 비밀번호와 동일합니다.");
		}		
		
		if(bindingResult.hasErrors()) {	
			return "mypage/changePwForm";
		}
		
		LoginMember loginMember = (LoginMember)session.getAttribute("loginMember");
		
		boolean result = memberSVC.changePw( loginMember.getEmail(), changePwForm.getPrepw(), changePwForm.getPostpw());
		if(result) {
			//세션 제거후 로긴화면 이동
			session.invalidate();
			return "redirect:/login";
		}
		bindingResult.reject("error.member.changePw", "비밀번호 변경 실패!");
		
		return "mypage/changePwForm";
	}
	
	/**
	 * 프로파일 조회
	 * @param session
	 * @param model
	 * @return
	 */
	@GetMapping("/profile")
	public String profileEditForm( HttpSession session, Model model ) {
		LoginMember loginMember = (LoginMember)session.getAttribute("loginMember");
		log.info("loginmember:{}",loginMember);
		MemberDTO memberDTO = memberSVC.findByEmail(loginMember.getEmail());
		UpLoadFileDTO upLoadFileDTO = upLoadFileDAO.getFileByRid(String.valueOf(loginMember.getId()));
		
		ProfileForm profileForm = new ProfileForm();
		profileForm.setNickname(memberDTO.getNickname());
		profileForm.setSavedImgFile(upLoadFileDTO);
		
		model.addAttribute("profileForm", profileForm);
		return "mypage/profileEditForm";
	}
	
	/**
	 * 별칭수정
	 * @param nickname
	 * @param session
	 * @return
	 */
	@ResponseBody
	@PatchMapping("/profile/nickname")
	public JsonResult<String> profileEdit(
			@RequestBody String nickname,
			HttpSession session) {
		
		log.info("profile:{}",nickname);
		LoginMember loginMember = (LoginMember)session.getAttribute("loginMember");
		
		JsonResult<String> jsonResult = null;
		boolean result = memberSVC.changeNickname(loginMember.getId(), nickname);
		if(result) {
			loginMember.setNickname(nickname);
			session.setAttribute("loginMember", loginMember);
			jsonResult = new JsonResult<String>("00","ok",nickname);
		}else {
			jsonResult = new JsonResult<String>("01","nok",nickname);
		}
		return jsonResult;
	}	
}
