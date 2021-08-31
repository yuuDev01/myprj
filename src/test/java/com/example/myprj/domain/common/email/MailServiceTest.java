package com.example.myprj.domain.common.email;

import java.io.File;
import java.nio.file.FileSystem;
import java.util.ArrayList;
import java.util.List;

import javax.activation.DataSource;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;

import com.example.myprj.domain.common.mail.MailService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class MailServiceTest {
	
	@Autowired
	private MailService ms;
	
	@Test
	@DisplayName("메일전송")
	void sendMail() {
		ms.sendMail("bisyj31@naver.com", "메일제목", "메일본문");
	}
	
	@Test
	@DisplayName("메일전송 with 첨부")
	void sendMailWithAttach() {
		FileSystemResource res = new FileSystemResource(new File("d:/sample.jpg"));
		List<File> files = new ArrayList<File>();
		files.add(res.getFile());
		ms.sendMailWithAttatch("bisyj31@naver.com", "메일제목 : 첨부", "메일본문", files);
	}
	
	@Test
	@DisplayName("메일전송 wit 인라인")
	void sendMailWithInline() {
		FileSystemResource res = new FileSystemResource(new File("d:/sample.jpg"));
		List<File> files = new ArrayList<File>();
		files.add(res.getFile());
		ms.sendMailWithInline("bisyj31@naver.com", "메일제목 : 인라인", "메일본문" ,files);
	}
	
}
