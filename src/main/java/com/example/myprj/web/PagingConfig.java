package com.example.myprj.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.myprj.domain.common.paging.FindCriteria;
import com.example.myprj.domain.common.paging.PageCriteria;
import com.example.myprj.domain.common.paging.RecordCriteria;

@Configuration
public class PagingConfig {

   static final int REC10 = 10;  //한페이지에 보여줄 레코드수
   static final int PAGE10 = 10;   //한페이지에 보여줄 페이지수
   
   static final int REC5 = 5;  //한페이지에 보여줄 레코드수
   static final int PAGE5 = 5;   //한페이지에 보여줄 페이지수
   
   @Bean(name = "rec10")
   public RecordCriteria rc10() {
      return new RecordCriteria(REC10);
   }
   @Bean(name = "pc10")
   public PageCriteria pc10() {
      return new PageCriteria(rc10(), PAGE10);
   }
   @Bean(name = "rec5")
   public RecordCriteria rc5() {
      return new RecordCriteria(REC5);
   }
   @Bean(name = "pc5")
   public PageCriteria pc5() {
      return new PageCriteria(rc5(), PAGE5);
   }   
   @Bean(name = "fc10")
   public FindCriteria fc10() {
      return new FindCriteria(rc10(), PAGE10);
   }
   @Bean(name = "fc5")
   public FindCriteria fc5() {
      return new FindCriteria(rc5(), PAGE5);
   }   
   
}
