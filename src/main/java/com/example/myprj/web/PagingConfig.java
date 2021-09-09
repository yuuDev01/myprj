package com.example.myprj.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.myprj.domain.common.paging.PageCriteria;
import com.example.myprj.domain.common.paging.RecordCriteria;

@Configuration
public class PagingConfig {
	
	static final int REC10 = 10;  //한페이지에 보여줄 레코드수
	static final int PAGE10 = 10;  //한페이지에 보여줄 페이지수
	
	static final int REC5 = 5;  //한페이지에 보여줄 레코드수
	static final int PAGE5 = 5;  //한페이지에 보여줄 페이지수
	
	
	//bean으로는 객체가 1개씩만 만들어짐
	@Bean(name="rec10")
	public RecordCriteria rc_10() {
		return new RecordCriteria(REC10);
	}
	
	@Bean(name="pc10")
	public PageCriteria pc_10() {
		return new PageCriteria(rc_10(), PAGE10);
	}
	
	@Bean(name="rec5")
	public RecordCriteria rc_5() {
		return new RecordCriteria(REC5);
	}
	
	@Bean(name="pc5")
	public PageCriteria pc_5() {
		return new PageCriteria(rc_5(), PAGE5);
	}
	
	
}
