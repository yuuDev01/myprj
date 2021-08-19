package com.example.myprj.web;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.myprj.web.form.LoginForm;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class HomeController {
	@GetMapping("/")
	public String home() {
		return "home";
	}
	
	//로그인 양식
	@GetMapping("/login")
	public String loginForm(@ModelAttribute LoginForm loginForm) {
		return "loginForm";
	}
	
	//로그인 처리
	@PostMapping("/login")
	public String login(
			@Valid @ModelAttribute LoginForm loginForm,
			BindingResult bindingResult) {
		log.info("LoginForm:{}",loginForm);
		if(bindingResult.hasErrors()){
		log.info("BindingResult:{}",bindingResult);
		return "loginForm";
	}
		return "home";
	}
	
	//로그아웃
	@GetMapping("/logout")
	public String logout() {
		//세션제거
		return "home";
	}
}
