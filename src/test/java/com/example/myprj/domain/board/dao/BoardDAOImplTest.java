package com.example.myprj.domain.board.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import com.example.myprj.domain.board.dto.BoardDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class BoardDAOImplTest {

	@Autowired
	private BoardDAOImpl boardDAOImpl;
	
	@Test
	@DisplayName("원글 생성")
	@Disabled
	void write() {
		
		BoardDTO boardDTO = new BoardDTO();
		boardDTO.setBcategory("A0502");
		boardDTO.setBtitle("제목-2");
		boardDTO.setBid(21L);
		boardDTO.setBemail("test3@test.com");
		boardDTO.setBnickname("테스터3");
		boardDTO.setBcontent("원글생성 테스트중...");
		
		Long bnum = boardDAOImpl.write(boardDTO);
		log.info("원글생성 bnum:{}",bnum);
	}
	
	@Test
	@DisplayName("게시글 수정")
	@Disabled
	void modifyItem() {
		Long bnum = 5L;		
		
		BoardDTO boardDTO = new BoardDTO();
		boardDTO.setBcategory("A0502");
		boardDTO.setBtitle("제목수정2");
		boardDTO.setBcontent("내용수정2");
		
		boardDAOImpl.modifyItem(bnum, boardDTO);
		
		BoardDTO modifiedBoardDTO = boardDAOImpl.itemDetail(bnum);
		
		Assertions.assertThat(boardDTO.getBcategory()).isEqualTo(modifiedBoardDTO.getBcategory());
		Assertions.assertThat(boardDTO.getBtitle()).isEqualTo(modifiedBoardDTO.getBtitle());
		Assertions.assertThat(boardDTO.getBcontent()).isEqualTo(modifiedBoardDTO.getBcontent());
	}
	
	@Test
	@DisplayName("게시글 수정 wrong bnum 예외처리")
	void modifyItemThrow() {
		Long bnum = 10L;		
		
		BoardDTO boardDTO = new BoardDTO();
		boardDTO.setBcategory("A0502");
		boardDTO.setBtitle("제목수정2");
		boardDTO.setBcontent("내용수정2");
		
		org.junit.jupiter.api.Assertions.assertThrows(
				InvalidDataAccessApiUsageException.class, 
				()->boardDAOImpl.modifyItem(bnum, boardDTO)
		);
	}
}