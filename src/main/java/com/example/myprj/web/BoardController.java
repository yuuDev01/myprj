package com.example.myprj.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/bbs")
public class BoardController {
	
	//게시글 목록
	@GetMapping("/list")
	public String list() {
		return "bbs/list";
	}
	
	//게시글 작성
	@GetMapping("/")
	public String writeForm() {
		return "bbs/writeForm";
	}

	//게시글 처리
	@PostMapping("/")
	public String write() {
		return "redirect:/bbs/list";
	}
}
