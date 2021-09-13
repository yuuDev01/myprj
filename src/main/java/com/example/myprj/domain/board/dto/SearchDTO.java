package com.example.myprj.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchDTO {
	private String category; //게시판 카테고리
	private int starcRec; //시작레코드
	private int endRec; //종료레코드
	private String searchType; //검색유형
	private String keyword; //검색어
}
