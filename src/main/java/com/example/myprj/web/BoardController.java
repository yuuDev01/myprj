package com.example.myprj.web;

import java.io.IOException;
import java.util.ArrayList;
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
import com.example.myprj.domain.common.dto.MetaOfUploadFile;
import com.example.myprj.domain.common.dto.UpLoadFileDTO;
import com.example.myprj.domain.common.file.FileStore;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/bbs")
public class BoardController {

	private final BoardSVC boardSVC;
	private final CodeDAO codeDAO;
	private final FileStore fileStore;
	
	@ModelAttribute("category")
	public List<Code> hobby(){
		List<Code> list = codeDAO.getCode("A05");
		log.info("code-category:{}",list);
		return list;
	}
	
	
	//원글 작성 양식
	@GetMapping("/")
	public String writeForm(
			//@ModelAttribute WriteForm wrtieForm
			Model model,
			HttpServletRequest request
			) {
		
		WriteForm writeForm = new WriteForm();
		
		HttpSession session = request.getSession(false);
		if(session != null && session.getAttribute("loginMember") != null) {
			LoginMember loginMember = 
					(LoginMember)session.getAttribute("loginMember");
			
			writeForm.setBid(loginMember.getId());
			writeForm.setBemail(loginMember.getEmail());
			writeForm.setBnickname(loginMember.getNickname());
		}
		
		model.addAttribute("writeForm",writeForm);
		return "bbs/writeForm";
	}
	
	//원글 작성 처리
	@PostMapping("/")
	public String write(
			@Valid @ModelAttribute WriteForm writeForm,
			BindingResult bindingResult,
			RedirectAttributes redirectAttributes) throws IllegalStateException, IOException {
	
		if(bindingResult.hasErrors()) {
			return "bbs/writeForm";
		}
		
		BoardDTO boardDTO = new BoardDTO();
		BeanUtils.copyProperties(writeForm, boardDTO);
		
		//첨부파일 파일시스템에 저장후 메타정보 추출
		List<MetaOfUploadFile> storedFiles = fileStore.storeFiles(writeForm.getFiles());
		//UploadFileDTO 변환
		boardDTO.setFiles(convert(storedFiles));
		
		Long bnum = boardSVC.write(boardDTO);
		
		redirectAttributes.addAttribute("bnum", bnum);
		return "redirect:/bbs/{bnum}";
	}
	
	private UpLoadFileDTO convert(MetaOfUploadFile attatchFile) {
		UpLoadFileDTO uploadFileDTO = new UpLoadFileDTO();
		BeanUtils.copyProperties(attatchFile, uploadFileDTO);
		return uploadFileDTO;
	}
	
	private List<UpLoadFileDTO> convert(List<MetaOfUploadFile> uploadFiles) {
		List<UpLoadFileDTO> list = new ArrayList<>();
	
		for(MetaOfUploadFile file : uploadFiles) {
			UpLoadFileDTO uploadFIleDTO = convert(file);
			list.add( uploadFIleDTO );
		}		
		return list;
	}
	
	//답글 작성 양식
	@GetMapping("/reply")
	public String replyForm(Model model) {
		model.addAttribute("replyForm", new ReplyForm());
		return "bbs/replyForm";
	}
	
	//답글 작성 처리
	@PostMapping("/reply")
	public String reply(
			@Valid @ModelAttribute ReplyForm replyForm,
			BindingResult bindingResult) {
	
		if(bindingResult.hasErrors()) {
			return "bbs/replyForm";
		}
		
		return "redirect:/bbs/list";
	}	
	
	//게시글 상세
	@GetMapping("/{bnum}")
	public String detailItem(
			@PathVariable Long bnum,
			Model model) {
		
		model.addAttribute("detailItem", boardSVC.itemDetail(bnum));
		
		return "bbs/detailItem";
	}
	
	//게시글 목록
	@GetMapping("/list")
	public String list(Model model) {
		
		List<BoardDTO> list = boardSVC.list();
		
		model.addAttribute("list", list);
		
		return "bbs/list";
	}	
	
	//게시글 수정 양식
	@GetMapping("/{bnum}/edit")
	public String editForm(
			@PathVariable Long bnum,
			Model model) {
		
		model.addAttribute("item", boardSVC.itemDetail(bnum)) ;
		return "bbs/editForm";
	}
	
	//게시글 수정 처리
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
	public JsonResult<String> delItem(@PathVariable Long bnum) {

		boardSVC.delItem(bnum);
		return new JsonResult<String>("00", "ok", String.valueOf(bnum));
	}
	
}
