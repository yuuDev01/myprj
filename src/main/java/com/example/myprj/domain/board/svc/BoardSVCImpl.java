package com.example.myprj.domain.board.svc;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.myprj.domain.board.dao.BoardDAO;
import com.example.myprj.domain.board.dto.BoardDTO;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BoardSVCImpl implements BoardSVC{
	private final BoardDAO boardDAO;
	
	//원글작성
	@Override
		public Long write(BoardDTO boardDTO) {
			// TODO Auto-generated method stub
			Long bnum = boardDAO.write(boardDTO);
			return bnum;
		}
	
	//답글작성
	@Override
	public Long reply(BoardDTO boardDTO) {
		// TODO Auto-generated method stub
		Long bnum = boardDAO.reply(boardDTO);
		return bnum;
	}
	
	//게시글목록
	@Override
	public List<BoardDTO> list() {
		// TODO Auto-generated method stub
		List<BoardDTO> list = boardDAO.list();
		return list;
	}
	
	//게시글 상세
	@Override
	public BoardDTO itemDetail(Long bnum) {
		// TODO Auto-generated method stub
		BoardDTO boardDTO = boardDAO.itemDetail(bnum);
		return boardDTO;
	}
	
	//게시글 수정
	@Override
	public Long modifyItem(Long bnum, BoardDTO boardDTO) {
		// TODO Auto-generated method stub
		Long modifiedBnum = boardDAO.modifyItem(bnum, boardDTO);
		return modifiedBnum;
	}
	
	@Override
	public void delItem(Long bnum) {
		// TODO Auto-generated method stub
		boardDAO.delItem(bnum);
		
	}

	
	
}
