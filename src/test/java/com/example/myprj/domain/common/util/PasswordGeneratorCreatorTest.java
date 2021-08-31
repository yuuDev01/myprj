package com.example.myprj.domain.common.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PasswordGeneratorCreatorTest {
	
	@Test
	@DisplayName("비밀번호 생성")
	void generatorPassword() {
		log.info(PasswordGeneratorCreator.generator(7));
		log.info(PasswordGeneratorCreator.generator(10));
	}
}
