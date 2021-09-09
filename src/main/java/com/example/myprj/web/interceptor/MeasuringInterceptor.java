package com.example.myprj.web.interceptor;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.messaging.handler.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MeasuringInterceptor implements HandlerInterceptor {
	
	public static final String LOG_ID = "logId";
	
	//컨트롤로 호출전에 실행
	//반환값이 true : 다음인터셉터 혹은 컨트롤러 실행
	// 		 false : 컨트롤러 실행중지됨 
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// TODO Auto-generated method stub
		log.info("MeasuringInterSeptor.preHandle");
		
		String requestURI = request.getRequestURI();
		String uuid = UUID.randomUUID().toString();
		
		long startTime = System.currentTimeMillis();
		
		request.setAttribute(LOG_ID	, uuid);
		request.setAttribute("beginTime", System.currentTimeMillis());
		
		if(handler instanceof HandlerMethod) {
			HandlerMethod hm = (HandlerMethod)handler;
			
		}
		
		log.info("Request [{}][{}][{}]",uuid,requestURI,handler);
		
//		HandlerInterceptor.super.preHandle(request, response, handler);
		return true;
	}
	
	//컨트롤러 실행 후 호출됨
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		log.info("MeasuringInterSeptor.postHandle");
		String uuid = (String) request.getAttribute(LOG_ID);
		
		
		log.info("Request [{}][{}][{}]",uuid, modelAndView);
	}
	
	//뷰가 렌더링되고 클라이언트 응답 후 호출됨
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		log.info("MeasuringInterSeptor.afterCompletion");
		String requestURI = request.getRequestURI();
		String uuid = (String) request.getAttribute(LOG_ID);
		
		long beginTime = (long)request.getAttribute("beginTime");
		long endTime = System.currentTimeMillis();
		
		log.info("Request [{}][{}][실행시간:{}][{}]",uuid,requestURI,(endTime-beginTime),handler);
		
		if(ex != null) {
			log.error("afterCompletion error!!", ex);
			
		}
	}
}
