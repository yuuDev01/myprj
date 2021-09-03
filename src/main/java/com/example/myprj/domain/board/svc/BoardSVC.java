package com.example.myprj.domain.board.svc;

import java.util.List;

import com.example.myprj.domain.board.dto.BoardDTO;

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
	 * 게시글 수정
	 * @param bnum
	 * @param boardDTO
	 * @return
	 */
	Long modifyItem(Long bnum, BoardDTO boardDTO);
	
	/**
	 * 게시글 목록
	 * @return
	 */
	List<BoardDTO> list();
	
	/**
	 * 게시글 상세
	 * @param bnum
	 * @return
	 */
	BoardDTO itemDetail(Long bnum);
	
	/**
	 * 게시글 삭제
	 * @param bnum
	 */
	void delItem(Long bnum);
	
}
