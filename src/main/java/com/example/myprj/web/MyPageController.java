package com.example.myprj.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/mypage")
public class MyPageController {

	@GetMapping("/myinfo")
	public String mypage() {
		
		return "mypage/myinfo";
	}
}