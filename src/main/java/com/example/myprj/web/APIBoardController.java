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
	@DeleteMapping("/{cate}/{bnum}")
	public JsonResult<String> delItem(
			@PathVariable String cate,
			@PathVariable Long bnum) {

		boardSVC.delItem(cate,bnum);
		return new JsonResult<String>("00", "ok", String.valueOf(bnum));
	}
	
}
