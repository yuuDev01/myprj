package com.example.myprj.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.myprj.web.interceptor.LoginCheckInterceptor;
import com.example.myprj.web.interceptor.MeasuringInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer{
	
//	@Override
//	public void addInterceptors(InterceptorRegistry registry) {
//		// TODO Auto-generated method stub
////		WebMvcConfigurer.super.addInterceptors(registry);
//		registry.addInterceptor(new MeasuringInterceptor()).order(1).addPathPatterns("/**");
//	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// TODO Auto-generated method stub
		registry.addInterceptor(new LoginCheckInterceptor())
				.order(1)
				.addPathPatterns("/**")
				.excludePathPatterns(
						"/",
						"/login",
						"/logout",
						"/members/join",
						"/help/**",
						"/css/**",
						"/js/**",
						"/img/**",
						"/webjars/**",
						"/error/**");
	}
	

}
