package com.example.myprj.web;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.myprj.domain.board.svc.BoardSVC;
import com.example.myprj.domain.common.dao.UpLoadFileDAO;
import com.example.myprj.domain.common.file.FileStore;
import com.example.myprj.web.api.JsonResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/bbs")
public class APIBoardController {

	private final BoardSVC boardSVC;
	private final UpLoadFileDAO upLoadFileDAO;
	private final FileStore fileStore;
	
	//게시글 삭제
	@DeleteMapping("/{bnum}")
	public JsonResult<String> delItem(@PathVariable Long bnum) {

		boardSVC.delItem(bnum);
		return new JsonResult<String>("00", "ok", String.valueOf(bnum));
	}
	
	//첨부파일 삭제 by fid
	@DeleteMapping("/attach/{sfname}")
	public JsonResult<String> delFile(@PathVariable String sfname){
		
		if(fileStore.deleteFile(sfname)) {
			upLoadFileDAO.deleteFileBySfname(sfname);
		}else {
			return new JsonResult<String>("01","nok","파일삭제 실패!");
		}
		
		return new JsonResult<String>("00","ok","파일삭제 성공");
	}
}
