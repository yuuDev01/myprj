package com.example.myprj.domain.common.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class CodeDAOImplTest {

	@Autowired
	private CodeDAO codeDAO;
	
	@Test
	@DisplayName("코드조회")
	void code() {
		log.info("A01:{}",codeDAO.getCode("A01"));
		log.info("A02:{}",codeDAO.getCode("A02"));
		log.info("A03:{}",codeDAO.getCode("A03"));
		log.info("A04:{}",codeDAO.getCode("A04"));
	}
}
