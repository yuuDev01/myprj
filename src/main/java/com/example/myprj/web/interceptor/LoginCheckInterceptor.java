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
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
				throws Exception {
			// TODO Auto-generated method stub
		String requestURI = request.getRequestURI();
		log.info("LoginCheckInterceptor.preHandle 실행:{}", requestURI);
		HttpSession session = request.getSession(false);
		if(session == null || session.getAttribute("loginMember") == null) {
			log.info("미인증 요청시도");
			response.sendRedirect("/login?redirectUrl="+requestURI);
			return false;
		}
		return true;
	}
	
}
