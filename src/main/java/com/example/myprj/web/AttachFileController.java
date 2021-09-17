package com.example.myprj.web;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import com.example.myprj.domain.common.dao.UpLoadFileDAO;
import com.example.myprj.domain.common.dto.MetaOfUploadFile;
import com.example.myprj.domain.common.dto.UpLoadFileDTO;
import com.example.myprj.domain.common.file.FileStore;
import com.example.myprj.domain.member.svc.MemberSVC;
import com.example.myprj.web.api.JsonResult;
import com.example.myprj.web.form.LoginMember;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/attach")
public class AttachFileController {

	private final FileStore fileStore;
	private final UpLoadFileDAO upLoadFileDAO;
	private final MemberSVC memberSVC;
	
	/**
	 * 첨부파일 보기
	 * @param fname
	 * @return
	 * @throws MalformedURLException
	 */
	@ResponseBody //http응답 메세지 바디에 직접내용쓰기
	@ResponseStatus(HttpStatus.OK)  //응답코드 200
	@GetMapping("/view/{cate}/{sfname}")
	public Resource downLoadImage(
			@PathVariable String cate,
			@PathVariable String sfname) throws MalformedURLException  {
		Resource resource = new UrlResource("file:"+fileStore.getFullPath(cate,sfname));
		return resource;
	}
	
	/**
	 * 첨부파일 다운로드
	 * @param sfname
	 * @param ufname
	 * @return
	 * @throws MalformedURLException
	 */
	@GetMapping("/down/{cate}/{sfname}/{ufname}")
	public ResponseEntity<Resource> downloadAttach(
			@PathVariable String cate,
			@PathVariable String sfname,
			@PathVariable String ufname) throws MalformedURLException{
		
		Resource resource = new UrlResource("file:"+fileStore.getFullPath(cate,sfname));
		
		//한글파일명 깨짐 방지를위한 인코딩
		String encodeUploadFileName = UriUtils.encode(ufname, StandardCharsets.UTF_8);
		//Http응답 메세지 헤더에 첨부파일이 있음을 알림
		String contentDisposition = "attachment; filename="+ encodeUploadFileName;
		
		return ResponseEntity.ok()  //응답코드 200
												 .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
												 .body(resource);
	}
	
	
	/**
	 * 첨부 추가
	 * @param cate
	 * @param sfname
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)  //응답코드 200
	@PostMapping("/{cate}")
	public JsonResult<NewFileInfo> newFile(
			@PathVariable String cate,
			@ModelAttribute MultipartFile file,
			HttpSession session) throws IllegalStateException, IOException{
				
			LoginMember loginMember = (LoginMember)session.getAttribute("loginMember");
			UpLoadFileDTO upLoadFileDTO = new UpLoadFileDTO();
			//신규파일추가
			log.info("1:",cate,file);
			MetaOfUploadFile metaOfUploadFile = fileStore.storeFile(cate, file);
			if(metaOfUploadFile !=null ) {
				BeanUtils.copyProperties(metaOfUploadFile, upLoadFileDTO);
				upLoadFileDTO.setCode(cate);
				upLoadFileDTO.setRid(String.valueOf(loginMember.getId()));
				upLoadFileDAO.addFile(upLoadFileDTO);
			}else {
				return new JsonResult<NewFileInfo>("01","nok",new NewFileInfo(cate,upLoadFileDTO.getStore_fname(),"파일생성 실패!"));
			}
		
		return new JsonResult<NewFileInfo>("00","ok",new NewFileInfo(cate,upLoadFileDTO.getStore_fname(),"성공"));
	}
	
	/**
	 * 첨부 변경
	 * @param cate
	 * @param sfname
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)  //응답코드 200
	@PutMapping("/{cate}/{sfname}")
	public JsonResult<NewFileInfo> changeFile(
			@PathVariable String cate,
			@PathVariable String sfname,
			@ModelAttribute MultipartFile file,
			HttpSession session) throws IllegalStateException, IOException{
		
		if(upLoadFileDAO.getFileBySfname(sfname) == null) {
			return new JsonResult<NewFileInfo>("01","nok",new NewFileInfo(null,null,"존재하지 않는 파일"));
		};
		
		LoginMember loginMember = (LoginMember)session.getAttribute("loginMember");
		
		UpLoadFileDTO upLoadFileDTO = new UpLoadFileDTO();
		//기존파일 삭제
		if(fileStore.deleteFile(cate,sfname)) {
			upLoadFileDAO.deleteFileBySfname(sfname);
			
			//신규파일추가
			MetaOfUploadFile metaOfUploadFile = fileStore.storeFile(cate, file);
			BeanUtils.copyProperties(metaOfUploadFile, upLoadFileDTO);
			upLoadFileDTO.setCode(cate);
			upLoadFileDTO.setRid(String.valueOf(loginMember.getId()));
			upLoadFileDAO.addFile(upLoadFileDTO);
		}else {
			return new JsonResult<NewFileInfo>("02","nok",new NewFileInfo(null,null,"기존 파일삭제 실패"));
		}
		
		return new JsonResult<NewFileInfo>("00","ok",new NewFileInfo(cate,upLoadFileDTO.getStore_fname(),"성공"));
	}
	
	/**
	 * 첨부파일 삭제
	 * @param cate
	 * @param sfname
	 * @return
	 */
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)  //응답코드 200
	@DeleteMapping("/{cate}/{sfname}")
	public JsonResult<String> delFile(
			@PathVariable String cate,
			@PathVariable String sfname){
		
		log.info("cate={},sfname={}",cate,sfname);
		if(fileStore.deleteFile(cate,sfname)) {
			upLoadFileDAO.deleteFileBySfname(sfname);
		}else {
			return new JsonResult<String>("01","nok","파일삭제 실패!");
		}
		
		return new JsonResult<String>("00","ok","파일삭제 성공");
	}

	@Data @AllArgsConstructor @NoArgsConstructor
	static class NewFileInfo{
		private String cate;
		private String sfname;
		private String errmsg;
	}
}