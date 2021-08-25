package com.example.myprj.domain.member.svc;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.myprj.domain.member.dto.MemberDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class MemberSVCImplTest {

	@Autowired
	private MemberSVC mSVC;
	
	@Test
	@DisplayName("이메일 중복체크")
	void isExistEmail() {
		boolean result = mSVC.isExistEmail("test@test.com");
		Assertions.assertThat(result).isEqualTo(true);
		boolean result2 = mSVC.isExistEmail("zzz@test.com");
		Assertions.assertThat(result2).isEqualTo(false);
	}
	
	@Test
	@DisplayName("로그인체크")
	void isLogin() {
		MemberDTO mdto = mSVC.isLogin("test@test.com", "1234");
		Assertions.assertThat(mdto.getEmail()).isEqualTo("test@test.com");
		
		MemberDTO mdto2 = mSVC.isLogin("zzz@test.com", "1234");
		log.info("mdto2:{}",mdto2);
		Assertions.assertThat(mdto2).isEqualTo(null);
	}
}