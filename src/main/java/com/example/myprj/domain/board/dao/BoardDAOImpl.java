package com.example.myprj.domain.board.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.myprj.domain.board.dto.BoardDTO;
import com.example.myprj.domain.board.dto.SearchDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class BoardDAOImpl implements BoardDAO {

	private final JdbcTemplate jt;
	
	//원글 생성
	@Override
	public Long write(BoardDTO boardDTO) {
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO board ( ");
		sql.append("  bnum, ");
		sql.append("  bcategory, ");
		sql.append("  btitle, ");
		sql.append("  bid, ");
		sql.append("  bemail, ");
		sql.append("  bnickname, ");
		sql.append("  bhit, ");
		sql.append("  bcontent, ");
		sql.append("  bgroup, ");
		sql.append("  bstep, ");
		sql.append("  bindent ");
		sql.append(") VALUES ( ");
		sql.append("  board_bnum_seq.nextval, ");
		sql.append("  ?, ");
		sql.append("  ?, ");
		sql.append("  ?, ");
		sql.append("  ?, ");
		sql.append("  ?, ");
		sql.append("  0, "); //조회수
		sql.append("  ?, ");
		sql.append("  BOARD_BNUM_SEQ.currval, "); //답글그룹
		sql.append("  0, "); //답글순서
		sql.append("  0 ");  //답글들여쓰기
		sql.append(") ");

		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		jt.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement pstmt = con.prepareStatement(
						sql.toString(),
						new String[] {"bnum"});
				
				pstmt.setString(1, boardDTO.getBcategory());
				pstmt.setString(2, boardDTO.getBtitle());
				pstmt.setLong(3, boardDTO.getBid());
				pstmt.setString(4, boardDTO.getBemail());
				pstmt.setString(5, boardDTO.getBnickname());
				pstmt.setString(6, boardDTO.getBcontent());
					
				return pstmt;
			}
		}, keyHolder);
		
		return ((BigDecimal)keyHolder.getKeys().get("bnum")).longValue();
	}

	//답글 생성
	@Override
	public Long reply(BoardDTO boardDTO) {
		
		//부모글의 bgroup중 bstep이 부모글의 bstep보다 큰 게시글 bstep + 1
		updateStep(boardDTO.getBgroup(), boardDTO.getBstep());
		
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO board ( ");
		sql.append("  bnum, ");
		sql.append("  bcategory, ");
		sql.append("  btitle, ");
		sql.append("  bid, ");
		sql.append("  bemail, ");
		sql.append("  bnickname, ");
		sql.append("  bhit, ");
		sql.append("  bcontent, ");
		sql.append("  pbnum, ");
		sql.append("  bgroup, ");
		sql.append("  bstep, ");
		sql.append("  bindent ");
		sql.append(") VALUES ( ");
		sql.append("  board_bnum_seq.nextval, ");
		sql.append("  ?, ");
		sql.append("  ?, ");
		sql.append("  ?, ");
		sql.append("  ?, ");
		sql.append("  ?, ");
		sql.append("  0, "); //조회수
		sql.append("  ?, ");
		sql.append("  ?, "); //부모글
		sql.append("  ?, "); //답글그룹
		sql.append("  ?, "); //답글순서
		sql.append("  ? ");  //답글들여쓰기
		sql.append(") ");

		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		jt.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement pstmt = con.prepareStatement(
						sql.toString(),
						new String[] {"bnum"});
				
				pstmt.setString(1, boardDTO.getBcategory());
				pstmt.setString(2, boardDTO.getBtitle());
				pstmt.setLong(3, boardDTO.getBid());
				pstmt.setString(4, boardDTO.getBemail());
				pstmt.setString(5, boardDTO.getBnickname());
				pstmt.setString(6, boardDTO.getBcontent());
				pstmt.setLong(7, boardDTO.getPbnum());
				pstmt.setLong(8, boardDTO.getBgroup());
				pstmt.setLong(9, boardDTO.getBstep() + 1);
				pstmt.setLong(10, boardDTO.getBindent() + 1);
					
				return pstmt;
			}
		}, keyHolder);
		
		return ((BigDecimal)keyHolder.getKeys().get("bnum")).longValue();
	}

	private void updateStep(Long bgroup, Long bstep) {
		StringBuilder sql = new StringBuilder();
		sql.append("update board ");
		sql.append("   set bstep = bstep + 1 ");
		sql.append(" where bgroup = ? ");
		sql.append("   and bstep > ? ");
		
		jt.update(sql.toString(), bgroup,bstep);
	}

	//게시글 수정
	@Override
	public Long modifyItem(Long bnum, BoardDTO boardDTO) {
		
		StringBuffer sql = new StringBuffer();
		sql.append("update board ");
		sql.append("   set bcategory = ?, ");
		sql.append("       btitle = ? , ");
		sql.append("       bcontent = ?, ");
		sql.append("       budate = systimestamp ");
		sql.append(" where bnum = ? ");
		
		int rows = jt.update(sql.toString(), 
													boardDTO.getBcategory(),
													boardDTO.getBtitle(),
													boardDTO.getBcontent(),
													bnum);
		
		if(rows != 1) {
			 throw new IllegalArgumentException(bnum + " 번 게시글 번호를 찾을 수 없습니다!");
		}
		
		log.info("게시글 수정 rows:"+rows);
		return bnum;
	}

	//게시글 목록
	@Override
	public List<BoardDTO> list() {
		StringBuffer sql = new StringBuffer();
		sql.append("select bnum,  ");
		sql.append("       bcategory,  ");
		sql.append("       btitle,  ");
		sql.append("       bnickname,  ");
		sql.append("       bid, ");
		sql.append("       bemail,  ");
		sql.append("       bhit, ");
		sql.append("       pbnum, ");
		sql.append("       bgroup, ");
		sql.append("       bstep, ");
		sql.append("       bindent, ");
		sql.append("       status, ");
		sql.append("       bcdate, ");
		sql.append("       budate ");
		sql.append("  from board  ");
		sql.append(" order by bgroup desc, bstep asc ");
		
		List<BoardDTO> list = jt.query(
				sql.toString(), 
				new BeanPropertyRowMapper<>(BoardDTO.class)
				);	
		
		return list;
	}
	
	//게시글 목록
	@Override
	public List<BoardDTO> list(int startRec, int endRec) {
		StringBuffer sql = new StringBuffer();
		sql.append("select t1.* ");
		sql.append("  from (select row_number() over(order by bgroup desc, bstep asc) as num, ");
		sql.append("               bnum,    ");
		sql.append("               bcategory,   ");
		sql.append("               btitle,    ");
		sql.append("               bnickname,   ");
		sql.append("               bid,   ");
		sql.append("               bemail,    ");
		sql.append("               bhit,  ");
		sql.append("               pbnum,   ");
		sql.append("               bgroup,  ");
		sql.append("               bstep,   ");
		sql.append("               bindent,   ");
		sql.append("               status,  ");
		sql.append("               bcdate,  ");
		sql.append("               budate   ");
		sql.append("          from board) t1  ");
		sql.append(" where num between ? and ?  ");
		
		List<BoardDTO> list = jt.query(
				sql.toString(), 
				new BeanPropertyRowMapper<>(BoardDTO.class),
				startRec,endRec
				);	
		
		return list;
	}	
	//게시판 전체 검색 목록
	@Override
	public List<BoardDTO> list(int startRec, int endRec, String searchType, String keyword) {

		StringBuffer sql = new StringBuffer();
		sql.append("select t1.* ");
		sql.append("  from (select row_number() over(order by bgroup desc, bstep asc) as num, ");
		sql.append("               bnum,    ");
		sql.append("               bcategory,   ");
		sql.append("               btitle,    ");
		sql.append("               bnickname,   ");
		sql.append("               bid,   ");
		sql.append("               bemail,    ");
		sql.append("               bhit,  ");
		sql.append("               pbnum,   ");
		sql.append("               bgroup,  ");
		sql.append("               bstep,   ");
		sql.append("               bindent,   ");
		sql.append("               status,  ");
		sql.append("               bcdate,  ");
		sql.append("               budate   ");
		sql.append("          from board t1");
		sql.append("         where ");
		
		switch (searchType) {
		case "TC": //제목+내용
			sql.append("( t1.btitle  like '%" + keyword + "%' ");
			sql.append("   or t1.bcontent like '%" + keyword + "%' ) ");
			break;
		case "T":	//제목
			sql.append("t1.btitle  like '%" + keyword + "%' ");
			break;
		case "C":	//내용
			sql.append("t1.bcontent  like '%" + keyword + "%' ");
			break;
		case "N": //별칭
			sql.append("t1.bnickname  like '%" + keyword + "%'" );
			break;
		case "E":  //아이디(이메일)
			sql.append("t1.bemail  like '%" + keyword + "%'" );
			break;
		case "A":  //전체			
			sql.append("( t1.btitle  like '%" + keyword + "%' ");
			sql.append("   or t1.bcontent like '%" + keyword + "%' ");
			sql.append("   or t1.bnickname like '%" + keyword + "%' ");
			sql.append("   or t1.bemail like '%" + keyword + "%' )");
			break;

		default:
			break;
		}				
		sql.append(") t1  ");		
		sql.append(" where num between ? and ?  ");
		
	
		List<BoardDTO> list = jt.query(
				sql.toString(), 
				new BeanPropertyRowMapper<>(BoardDTO.class),
				startRec, endRec
				);	
		
		return list;
	}
	//게시판 카테고리별 요청페이지 목록
	@Override
	public List<BoardDTO> list(String bcategory, int startRec, int endRec) {
		StringBuffer sql = new StringBuffer();
		sql.append("select t1.* ");
		sql.append("  from (select row_number() over(order by bgroup desc, bstep asc) as num, ");
		sql.append("               bnum,    ");
		sql.append("               bcategory,   ");
		sql.append("               btitle,    ");
		sql.append("               bnickname,   ");
		sql.append("               bid,   ");
		sql.append("               bemail,    ");
		sql.append("               bhit,  ");
		sql.append("               pbnum,   ");
		sql.append("               bgroup,  ");
		sql.append("               bstep,   ");
		sql.append("               bindent,   ");
		sql.append("               status,  ");
		sql.append("               bcdate,  ");
		sql.append("               budate   ");
		sql.append("          from board ");
		sql.append("         where bcategory = ? ) t1  ");
		sql.append(" where num between ? and ?  ");
		
		List<BoardDTO> list = jt.query(
				sql.toString(), 
				new BeanPropertyRowMapper<>(BoardDTO.class),
				bcategory, startRec, endRec
				);	
		
		return list;
	}
	
	@Override
	public List<BoardDTO> list(SearchDTO searchDTO) {
		StringBuffer sql = new StringBuffer();
		sql.append("select t1.* ");
		sql.append("  from (select row_number() over(order by bgroup desc, bstep asc) as num, ");
		sql.append("               bnum,    ");
		sql.append("               bcategory,   ");
		sql.append("               btitle,    ");
		sql.append("               bnickname,   ");
		sql.append("               bid,   ");
		sql.append("               bemail,    ");
		sql.append("               bhit,  ");
		sql.append("               pbnum,   ");
		sql.append("               bgroup,  ");
		sql.append("               bstep,   ");
		sql.append("               bindent,   ");
		sql.append("               status,  ");
		sql.append("               bcdate,  ");
		sql.append("               budate   ");
		sql.append("          from board t1");
		sql.append("         where bcategory = ? ");
		
		switch (searchDTO.getSearchType()) {
		case "TC": //제목+내용
			sql.append("and ( t1.btitle  like '%" + searchDTO.getKeyword() + "%' ");
			sql.append("   or t1.bcontent like '%" + searchDTO.getKeyword() + "%' ) ");
			break;
		case "T":	//제목
			sql.append("and t1.btitle  like '%" + searchDTO.getKeyword() + "%' ");
			break;
		case "C":	//내용
			sql.append("and t1.bcontent  like '%" + searchDTO.getKeyword() + "%' ");
			break;
		case "N": //별칭
			sql.append("and t1.bnickname  like '%" + searchDTO.getKeyword() + "%'" );
			break;
		case "E":  //아이디(이메일)
			sql.append("and t1.bemail  like '%" + searchDTO.getKeyword() + "%'" );
			break;
		case "A":  //전체			
			sql.append("and ( t1.btitle  like '%" + searchDTO.getKeyword() + "%' ");
			sql.append("   or t1.bcontent like '%" + searchDTO.getKeyword() + "%' ");
			sql.append("   or t1.bnickname like '%" + searchDTO.getKeyword() + "%' ");
			sql.append("   or t1.bemail like '%" + searchDTO.getKeyword() + "%' )");
			break;

		default:
			break;
		}				
		sql.append(") t1  ");		
		sql.append(" where num between ? and ?  ");
		
	
		List<BoardDTO> list = jt.query(
				sql.toString(), 
				new BeanPropertyRowMapper<>(BoardDTO.class),
				searchDTO.getCategory(), searchDTO.getStarcRec(), searchDTO.getEndRec()
				);	
		
		return list;
	}
	//게시글 상세
	@Override
	public BoardDTO itemDetail(Long bnum) {
		StringBuffer sql = new StringBuffer();
		sql.append("select bnum,  ");
		sql.append("       bcategory,  ");
		sql.append("       btitle,  ");
		sql.append("       bnickname,  ");
		sql.append("       bid, ");
		sql.append("       bemail,  ");
		sql.append("       bhit, ");
		sql.append("       bcontent, ");
		sql.append("       pbnum, ");
		sql.append("       bgroup, ");
		sql.append("       bstep, ");
		sql.append("       bindent, ");
		sql.append("       status, ");
		sql.append("       bcdate, ");
		sql.append("       budate ");
		sql.append("  from board  ");
		sql.append(" where bnum = ? ");
		
		BoardDTO boardDTO =	jt.queryForObject(
			sql.toString(), 
			new BeanPropertyRowMapper<>(BoardDTO.class),
			bnum
		);
		return boardDTO;
	}

	//게시글 삭제
	@Override
	public void delItem(Long bnum) {
		StringBuffer sql = new StringBuffer();
		sql.append("delete from board ");
		sql.append(" where bnum = ? ");
		
		if(jt.update(sql.toString(), bnum) != 1) {
			throw new IllegalArgumentException(bnum + " 번 게시글 번호를 찾을 수 없습니다!");
		};
	}
	
	//조회수 증가
	@Override
	public void updateBhit(Long bnum) {
		StringBuffer sql = new StringBuffer();
		sql.append("update board ");
		sql.append("	 set bhit = bhit + 1 ");
		sql.append(" where bnum = ? ");
		
		jt.update(sql.toString(), bnum);
		
	}
	
	//게시판 전체 레코드수
	@Override
	public long totoalRecordCount() {
		
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) ");
		sql.append("	from board ");
		
		long totalCount = jt.queryForObject(sql.toString(), Long.class);
		return totalCount;
	}
	
	//게시판 전체 검색 레코드 수
	@Override
	public long totoalRecordCount(String searchType, String keyword) {
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) ");
		sql.append("	from board t1");
		sql.append(" where ");
		
		switch (searchType) {
		case "TC": //제목+내용
			sql.append("( t1.btitle  like '%" + keyword + "%' ");
			sql.append("   or t1.bcontent like '%" + keyword + "%' ) ");
			break;
		case "T":	//제목
			sql.append("t1.btitle  like '%" + keyword + "%' ");
			break;
		case "C":	//내용
			sql.append("t1.bcontent  like '%" + keyword + "%' ");
			break;
		case "N": //별칭
			sql.append("t1.bnickname  like '%" + keyword + "%'" );
			break;
		case "E":  //아이디
			sql.append("t1.bemail  like '%" + keyword + "%'" );
			break;
		case "A":  //전체			
			sql.append("( t1.btitle  like '%" + keyword + "%' ");
			sql.append("   or t1.bcontent like '%" + keyword + "%' ");
			sql.append("   or t1.bnickname like '%" + keyword + "%' ");
			sql.append("   or t1.bemail like '%" + keyword + "%' )");
			break;

		default:
			break;
		}				
		long totalCount = jt.queryForObject(sql.toString(), Long.class);
		return totalCount;
	}
	
	//게시판 카테고리별 레코드수
	@Override
	public long totoalRecordCount(String bcategory) {
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) ");
		sql.append("	from board ");
		sql.append(" where bcategory = ?");
		
		long totalCount = jt.queryForObject(sql.toString(), Long.class, bcategory);
		return totalCount;
	}
	
	//게시판 카테고리별 검색결과 레코드수
	@Override
	public long totoalRecordCount(String bcategory, String searchType, String keyword) {
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) ");
		sql.append("	from board t1");
		sql.append(" where bcategory = ?");
		
		switch (searchType) {
		case "TC": //제목+내용
			sql.append("and ( t1.btitle  like '%" + keyword + "%' ");
			sql.append("   or t1.bcontent like '%" + keyword + "%' ) ");
			break;
		case "T":	//제목
			sql.append("and t1.btitle  like '%" + keyword + "%' ");
			break;
		case "C":	//내용
			sql.append("and t1.bcontent  like '%" + keyword + "%' ");
			break;
		case "N": //별칭
			sql.append("and t1.bnickname  like '%" + keyword + "%'" );
			break;
		case "E":  //아이디
			sql.append("and t1.bemail  like '%" + keyword + "%'" );
			break;
		case "A":  //전체			
			sql.append("and ( t1.btitle  like '%" + keyword + "%' ");
			sql.append("   or t1.bcontent like '%" + keyword + "%' ");
			sql.append("   or t1.bnickname like '%" + keyword + "%' ");
			sql.append("   or t1.bemail like '%" + keyword + "%' )");
			break;

		default:
			break;
		}				
		long totalCount = jt.queryForObject(sql.toString(), Long.class, bcategory);
		return totalCount;
	}
}