package com.example.myprj.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String redirectUrl = null;
		String requestURI = request.getRequestURI();
		String queryString = request.getQueryString();
		if(queryString !=null ) {
			StringBuffer str = new StringBuffer();
			str.append(requestURI)
				 .append("?")
				 .append(queryString);
			redirectUrl = str.toString();
		}else {
			redirectUrl = requestURI;
		}
		log.info("LoginCheckInterceptor.preHandle 실행:{}",redirectUrl);
		
		HttpSession session = request.getSession(false);
		if(session == null || session.getAttribute("loginMember") == null) {
			log.info("미인증 요청 시도!");
			response.sendRedirect("/login?redirectUrl=" +  redirectUrl);
			return false;
		}
		return true;
	}
	
}
