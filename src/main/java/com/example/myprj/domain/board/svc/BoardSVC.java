package com.example.myprj.domain.board.svc;

import java.util.List;

import com.example.myprj.domain.board.dto.BoardDTO;
import com.example.myprj.domain.board.dto.SearchDTO;

public interface BoardSVC {

	/**
	 * 원글 작성
	 * @param boardDTO
	 * @return
	 */
	Long write(BoardDTO boardDTO);
	
	/**
	 * 답글작성
	 * @param boardDTO
	 * @return
	 */
	Long reply(BoardDTO boardDTO);
		
	/**
	 * 게시글 목록
	 * @return
	 */
	List<BoardDTO> list();
	/**
	 * 게시글 전체 요청페이지 목록
	 * @param startRec
	 * @param endRec
	 * @return
	 */	
	List<BoardDTO> list(int startRec, int endRec);
	/**
	 * 게시글 카테고리별 요청페이지 목록
	 * @param category
	 * @param startRec
	 * @param endRec
	 * @return
	 */
	List<BoardDTO> list(String bcategory, int startRec, int endRec);
	/**
	 * 게시글 전체 검색 목록
	 * @param startRec
	 * @param endRec
	 * @param searchType
	 * @param keyword
	 * @return
	 */
	List<BoardDTO> list(int startRec, int endRec, String searchType, String keyword);
	
	/**
	 * 게시글 카테고리별 검색결과 목록
	 * @param searchDTO
	 * @return
	 */
	List<BoardDTO> list(SearchDTO searchDTO);
	/**
	 * 게시글 상세
	 * @param bnum
	 * @return
	 */
	BoardDTO itemDetail(Long bnum);
	
	/**
	 * 게시글 수정
	 * @param bnum
	 * @param boardDTO
	 * @return
	 */
	Long modifyItem(Long bnum, BoardDTO boardDTO);	
	
	/**
	 * 게시글 삭제
	 * @param bnum
	 */
	void delItem(Long bnum);
	
	/**
	 * 게시판 전체 레코드 수 
	 * @return
	 */
	long totoalRecordCount();
	/**
	 * 게시판 전체 검색 레코드 총수
	 * @param searchType
	 * @param keyword
	 * @return
	 */
	long totoalRecordCount(String searchType, String keyword);	
	/**
	 * 게시판 카테고리별 레코드 총수 
	 * @return
	 */	
	long totoalRecordCount(String bcategory);	
	
	/**
	 * 게시판 카테고리별 검색 레코드 총수 
	 * @return
	 */
	long totoalRecordCount(String bcategory, String searchType, String keyword);
}
