package com.example.myprj.web;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.myprj.domain.board.dto.BoardDTO;
import com.example.myprj.domain.board.svc.BoardSVC;
import com.example.myprj.domain.common.dao.CodeDAO;
import com.example.myprj.domain.common.mail.MailService;
import com.example.myprj.domain.member.svc.MemberSVC;
import com.example.myprj.web.api.JsonResult;
import com.example.myprj.web.form.Code;
import com.example.myprj.web.form.LoginMember;
import com.example.myprj.web.form.bbs.ReplyForm;
import com.example.myprj.web.form.bbs.WriteForm;
import com.example.myprj.web.form.member.EditForm;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/bbs")
@AllArgsConstructor
public class BoardController {
	
	private final BoardSVC boardSVC;
	private final CodeDAO codeDAO;
	
	@ModelAttribute("category")
	public List<Code> hobby(){
		List<Code> list = codeDAO.getCode("A05");
		log.info("code-hobby:{}",list);
		return list;
	}
	
	//게시글 목록
	@GetMapping("/list")
	public String list(Model model) {
		List<BoardDTO> list = boardSVC.list();
		model.addAttribute("list",list);
		return "bbs/list";
	}
	
	//게시글 작성
	@GetMapping("/")
	public String writeForm(
//			@ModelAttribute WriteForm writeForm,
			Model model,
			HttpServletRequest request
			) {
		WriteForm writeForm = new WriteForm();
		HttpSession session = request.getSession(false);
		log.info("sesion:",session);
		if(session != null && session.getAttribute("loginMember") != null) {
			LoginMember loginMember = (LoginMember)session.getAttribute("loginMember");
			writeForm.setBid(loginMember.getId());
			writeForm.setBemail(loginMember.getEmail());
			writeForm.setBnickname(loginMember.getNickname());
			
		}
		model.addAttribute("writeForm", writeForm); //새글을 생성 
			
		return "bbs/writeForm";
	}

	//게시글 처리
	@PostMapping("/")
	public String write(@Valid @ModelAttribute WriteForm writeForm,
			BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {
		if(bindingResult.hasErrors()) { //글 오류나면 다시 작성폼으로 이동
			return "bbs/writeForm";
		}
		BoardDTO boardDTO = new BoardDTO();		
		BeanUtils.copyProperties(writeForm, boardDTO);
		Long bnum = boardSVC.write(boardDTO);
		
		redirectAttributes.addAttribute("bnum",bnum);
		
		return "redirect:/bbs/{bnum}";
	}
	
	//답글 작성
	@GetMapping("/reply")
	public String replyForm(Model model) {
		model.addAttribute("replyForm", new ReplyForm());
		return "bbs/replyForm";
	}

	//답글 처리
	@PostMapping("/reply")
	public String reply(@ModelAttribute ReplyForm replyForm,
			BindingResult bindingResult) {
		if(bindingResult.hasErrors()) { //글 오류나면 다시 작성폼으로 이동
			return "bbs/replyForm";
		}
		return "redirect:/bbs/list";
	}
	
	//게시글 상세
	@GetMapping("/{bnum}")
	public String  detailItem(
			@PathVariable Long bnum,
			Model model) {
		model.addAttribute("detailItem", boardSVC.itemDetail(bnum));
		
		return "bbs/detialItem";
	}
	
	//게시글 수정 양식
	@GetMapping("/{bnum}/edit")
	public String editForm(@PathVariable Long bnum,
			Model model){
		model.addAttribute("item", boardSVC.itemDetail(bnum)); //값을 받아옴 
		return "bbs/editForm";
	}
	
	//게시글 수정  처리
	@PatchMapping("/{bnum}/edit")
	public String edit(
			@PathVariable Long bnum, 
			@Valid @ModelAttribute EditForm editForm,
			BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			return "bbs/editForm";	
		}
		return "redirect:/bbs/{bnum}";
	}
	
	//게시글 삭제
	@DeleteMapping("/{bnum}")
	@ResponseBody
	public JsonResult<String> delItem(@PathVariable long bnum) {
//		JsonResult<String> result = null;
		boardSVC.delItem(bnum);
		return new JsonResult<String>("00", "ok", String.valueOf(bnum));
		 
	}
}
