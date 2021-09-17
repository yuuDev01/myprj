package com.example.myprj.domain.member.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.myprj.domain.member.dto.MemberDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberDAOImpl implements MemberDAO {
	private final JdbcTemplate jt;
	
	//가입
	@Override
	public long insert(MemberDTO memberDTO) {
		
		StringBuffer sql = new StringBuffer();
		sql.append("insert into member( ");
		sql.append("  id, ");
		sql.append("  email, ");
		sql.append("  pw, ");
		sql.append("  tel, ");
		sql.append("  nickname, ");
		sql.append("  gender, ");
		sql.append("  region, ");
		sql.append("  birth, ");
		sql.append("  letter) ");
		sql.append("values ( ");
		sql.append("  member_id_seq.nextval, ");
		sql.append("  ?, ");
		sql.append("  ?, ");
		sql.append("  ?, ");
		sql.append("  ?, ");
		sql.append("  ?, ");
		sql.append("  ?, ");
		sql.append("  ?, ");
		sql.append("  ?) ");		
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jt.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement pstmt = con.prepareStatement(sql.toString(), new String[] {"id"} );
				pstmt.setString(1, memberDTO.getEmail());
				pstmt.setString(2, memberDTO.getPw());
				pstmt.setString(3, memberDTO.getTel());
				pstmt.setString(4, memberDTO.getNickname());
				pstmt.setString(5, memberDTO.getGender());
				pstmt.setString(6, memberDTO.getRegion());
				pstmt.setDate(7,memberDTO.getBirth());
				pstmt.setString(8, memberDTO.getLetter());
				
				return pstmt;
			}
		},keyHolder);
		
		return  keyHolder.getKeyAs(BigDecimal.class).longValue();
	}
	
	//취미 추가
	@Override
	public void addHobby(long id,List<String> hobbies) {
		StringBuffer sql = new StringBuffer();
		sql.append("insert into hobby (member_id, code_code) values ( ? , ? )");
		
		jt.batchUpdate(sql.toString(), new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setLong(1, id);
				ps.setString(2, hobbies.get(i));				
			}
			
			@Override
			public int getBatchSize() {
				return hobbies.size();
			}
		});
	}
	//취미 삭제
	@Override
	public void delHobby(long id) {
		String sql = "delete from hobby where member_id = ? ";
		jt.update(sql, id);
	}
	
	//취미 조회
	@Override
	public List<String> getHobby(long id) {
		
		String sql = "select code_code from hobby where member_id = ? ";
		return jt.queryForList(sql, String.class, id);
	}
	
	//회원조회 by id
	@Override
	public MemberDTO findByID(long id) {
		StringBuffer sql = new StringBuffer();
		
		sql.append("select  ");
		sql.append("    id, ");
		sql.append("    email,  ");
		sql.append("    pw, ");
		sql.append("    tel,  ");
		sql.append("    nickname, ");
		sql.append("    gender, ");
		sql.append("    region, ");
		sql.append("    birth,  ");
		sql.append("    letter, ");
		sql.append("    fid,  ");
		sql.append("    cdate,  ");
		sql.append("    udate ");
		sql.append("  from member ");
		sql.append(" where id = ?  ");
		
		MemberDTO mdto = jt.queryForObject(
				sql.toString(), new BeanPropertyRowMapper<>(MemberDTO.class),id);
		return mdto;
	}

	//회원조회 by email
	@Override
	public MemberDTO findByEmail(String email) {
		StringBuffer sql = new StringBuffer();
		
		sql.append("select  ");
		sql.append("    id, ");
		sql.append("    email,  ");
		sql.append("    pw, ");
		sql.append("    tel,  ");
		sql.append("    nickname, ");
		sql.append("    gender, ");
		sql.append("    region, ");
		sql.append("    birth,  ");
		sql.append("    letter, ");
		sql.append("    fid,  ");
		sql.append("    cdate,  ");
		sql.append("    udate ");
		sql.append("  from member ");
		sql.append(" where email = ?  ");
		
		MemberDTO mdto = jt.queryForObject(
				sql.toString(), new BeanPropertyRowMapper<>(MemberDTO.class),email);
		return mdto;
	}

	//이메일 존재유무
	@Override
	public boolean isExistEmail(String email) {
		boolean isExistEmail = false;
		
		String sql = "select count(email) from member where email = ? ";
		Integer cnt = jt.queryForObject(sql, Integer.class, email);
		
		if(cnt == 1) isExistEmail = true;
		
		return isExistEmail;
	}
	
	//로그인 체크
	@Override
	public boolean isLogin(String email, String pw) {
		boolean isLogin = false;
		
		StringBuffer sql = new StringBuffer();
		sql.append("select count(email) ");
		sql.append("  from member ");
		sql.append(" where email = ? ");
		sql.append("   and pw = ? ");
		sql.append("   and status is null ");
		
		Integer cnt = 
				jt.queryForObject(sql.toString(), Integer.class, email, pw);
		if(cnt == 1) isLogin = true;
		
		return isLogin;
	}
	
	@Override
	public List<MemberDTO> selectAll() {
		// TODO Auto-generated method stub
		return null;
	}

	//회원 수정
	@Override
	public void update(long id, MemberDTO memberDTO) {
		StringBuffer sql = new StringBuffer();
		sql.append("update member ");
		sql.append("   set tel = ?, ");
		sql.append("       nickname = ?, ");
		sql.append("       gender = ?, ");
		sql.append("       region = ?, ");
		sql.append("       birth = ?, ");
		sql.append("       letter = ?, ");
		sql.append("       udate = systimestamp ");
		sql.append("where id = ? ");
		
		jt.update(sql.toString(), 
				memberDTO.getTel(), 
				memberDTO.getNickname(),
				memberDTO.getGender(),
				memberDTO.getRegion(),
				memberDTO.getBirth(), 
				memberDTO.getLetter(),
				id);
		
	}

	// id로 회원 삭제
	@Override
	public void delete(long id) {
		String sql = "delete from member where id = ? ";
		jt.update(sql, id);
	}
	
	// email로 회원 삭제
	@Override
	public void delete(String email) {
		String sql = "delete from member where email = ? ";
		jt.update(sql, email);
	}
	
	// email 회원 탈퇴
	@Override
	public void outMember(String email, String pw) {
		StringBuffer sql = new StringBuffer();
		sql.append("update member ");
		sql.append("	 set status = 'D' ");
		sql.append(" where email = ? ");
		sql.append("   and pw = ? ");
		
		jt.update(sql.toString(), email, pw);
	}
	
	//회원아이디(이메일) 찾기
	@Override
	public String findEmail(String tel, Date birth) {
		StringBuffer sql = new StringBuffer();
		sql.append("select email from member ");
		sql.append(" where tel = ? ");
		sql.append("   and birth = ? ");
		
		String email = 
				jt.queryForObject(sql.toString(), String.class, tel,
						new SimpleDateFormat("yyyy-MM-dd").format(birth));
		return email;
	}
	
	//비밀번호찾기
	@Override
	public String findPw(String email, String tel, Date birth) {
		StringBuffer sql = new StringBuffer();
		sql.append("select pw from member ");
		sql.append(" where email = ? ");
		sql.append("   and tel = ? ");
		sql.append("   and birth = ? ");
		
		String pw = 
				jt.queryForObject(sql.toString(), String.class, email, tel, 
						new SimpleDateFormat("yyyy-MM-dd").format(birth));
		return pw;
	}
	
	//비밀번호 변경
	@Override
	public int changePw(String email, String prePw, String postPw) {
		StringBuffer sql = new StringBuffer();
		sql.append("update member ");
		sql.append("	 set pw = ? ");   //변경할 비밀번호
		sql.append(" where email = ? ");
		sql.append("   and pw = ? ");   //이전 비밀번호
		
		return jt.update(sql.toString(), postPw, email, prePw);
	}
	
	//별칭변경
	@Override
	public int changeNickname(Long id, String nickname) {
		StringBuffer sql = new StringBuffer();
		sql.append("update member ");
		sql.append("	 set nickname = ? ");   
		sql.append(" where id = ? ");
		
		return jt.update(sql.toString(),nickname, id);		
	}
}
