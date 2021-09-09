package com.example.myprj.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import com.example.myprj.domain.common.paging.PageCriteria;
import com.example.myprj.domain.common.paging.RecordCriteria;
import com.example.myprj.domain.member.svc.MemberSVC;
import com.example.myprj.web.api.JsonResult;
import com.example.myprj.web.form.Code;
import com.example.myprj.web.form.LoginMember;
import com.example.myprj.web.form.bbs.EditForm;
import com.example.myprj.web.form.bbs.ReplyForm;
import com.example.myprj.web.form.bbs.WriteForm;

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
	@Autowired
	@Qualifier("pc10")  //이름이 pc5인걸 가져다 쓰겠다
	private final PageCriteria pc;
//	private final RecordCriteria rc;
	
	@ModelAttribute("category")
	public List<Code> hobby(){
		List<Code> list = codeDAO.getCode("A05");
		log.info("code-category:{}",list);
		return list;
	}
	
	
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
	@GetMapping("/reply/{bnum}")
	public String replyForm(
			@PathVariable Long bnum,
			Model model,
			HttpServletRequest request) {		
		
		ReplyForm replyForm = new ReplyForm();
		
		//세션에서 회원 id,email,nickname가져오기
		HttpSession session = request.getSession(false);
		if(session != null && session.getAttribute("loginMember") != null) {
			LoginMember loginMember = 
					(LoginMember)session.getAttribute("loginMember");
			
			replyForm.setBid(loginMember.getId());
			replyForm.setBemail(loginMember.getEmail());
			replyForm.setBnickname(loginMember.getNickname());
		}
		
		//부모글의 글번호, 분류코드, 제목 가져오기
		BoardDTO pBoardDTO = boardSVC.itemDetail(bnum);
		replyForm.setPbnum(pBoardDTO.getBnum());
		replyForm.setBcategory(pBoardDTO.getBcategory());
		replyForm.setBtitle("답글 : " + pBoardDTO.getBtitle());
		
		model.addAttribute("replyForm", replyForm);
		
		return "bbs/replyForm";
	}
	
	//답글 작성 처리
	@PostMapping("/reply/{bnum}")
	public String reply(
			@PathVariable("bnum") Long pbnum,  //부모글
			@Valid @ModelAttribute ReplyForm replyForm,
			BindingResult bindingResult,
			RedirectAttributes redirectAttributes) throws IllegalStateException, IOException {
	
		if(bindingResult.hasErrors()) {
			return "bbs/replyForm";
		}
		
		BoardDTO boardDTO = new BoardDTO();
		BeanUtils.copyProperties(replyForm, boardDTO);
		
		//부모글의 bnum, bgroup, bstep, bindent
		BoardDTO pboardDTO = boardSVC.itemDetail(pbnum);
		boardDTO.setPbnum(pboardDTO.getBnum());
		boardDTO.setBgroup(pboardDTO.getBgroup());
		boardDTO.setBstep(pboardDTO.getBstep());
		boardDTO.setBindent(pboardDTO.getBindent());
		
		//첨부파일 파일시스템에 저장후 메타정보 추출
		List<MetaOfUploadFile> storedFiles = fileStore.storeFiles(replyForm.getFiles());
		//UploadFileDTO 변환
		boardDTO.setFiles(convert(storedFiles));
		
		Long rbnum = boardSVC.reply(boardDTO);		
		
		redirectAttributes.addAttribute("bnum", rbnum);
		return "redirect:/bbs/{bnum}";
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
	@GetMapping({"/list", "/list/{reqPage}"})
	public String list(
			@PathVariable(required = false) Integer reqPage,
			Model model) {
		
		//요청변수가 없으면 1
		if(reqPage == null) reqPage = 1;
		
		//사용자가 요청한 페이지번호
		pc.getRc().setReqPage(reqPage);
		//게시판 전체레코드수
		pc.setTotalRec(boardSVC.totalRecordCount());
		//페이징 계산
		pc.calculatePaging();
		
		List<BoardDTO> list = boardSVC.list(pc.getRc().getStartRec(),pc.getRc().getEndRec());
		
		model.addAttribute("list", list);
		model.addAttribute("pc", pc);
		
		return "bbs/list";
	}	
//	@GetMapping("/list")
//	public String list(Model model) {
//		
//		List<BoardDTO> list = boardSVC.list();
//		
//		model.addAttribute("list", list);
//		
//		return "bbs/list";
//	}	
	
	//게시글 수정 양식
	@GetMapping("/{bnum}/edit")
	public String editForm(
			@PathVariable Long bnum,
			Model model) {
			
		model.addAttribute("editForm", boardSVC.itemDetail(bnum)) ;
		return "bbs/editForm";
	}
	
	//게시글 수정 처리
	@PatchMapping("/{bnum}/edit")
	public String edit(
			@PathVariable Long bnum,
			@Valid @ModelAttribute EditForm editForm,
			BindingResult bindingResult,
			RedirectAttributes redirectAttributes) throws IllegalStateException, IOException {
		
		if(bindingResult.hasErrors()) {
			log.info("게시글수정처리오류:{}",bindingResult);
			return "bbs/editForm";
		}
		
		BoardDTO boardDTO = new BoardDTO();
		
		//첨부파일 파일시스템에 저장후 메타정보 추출
		List<MetaOfUploadFile> storedFiles = fileStore.storeFiles(editForm.getFiles());
		//UploadFileDTO 변환
		boardDTO.setFiles(convert(storedFiles));		
		BeanUtils.copyProperties(editForm, boardDTO);
		
		Long modifyedBnum = boardSVC.modifyItem(bnum, boardDTO);
		redirectAttributes.addAttribute("bnum", modifyedBnum);
		
		return "redirect:/bbs/{bnum}";
	}
	
}
