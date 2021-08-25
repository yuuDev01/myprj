package com.example.myprj.domain.member.dao;

import java.sql.Date;
import java.util.List;

import com.example.myprj.domain.member.dto.MemberDTO;

public interface MemberDAO {
	/**
	 * 가입
	 * @param memberDTO
	 * @return
	 */
	long insert(MemberDTO memberDTO);
	
	
	//MemberDTO findByIDPw(String id, String pw);
	/**
	 * 취미 추가
	 * @param id
	 * @param hobbies
	 */
	void addHobby(long id,List<String> hobbies);
	/**
	 * 취미 삭제
	 * @param id
	 */	
	void delHobby(long id);
	
	/**
	 * 조회 by id
	 * @param id
	 * @return
	 */
	MemberDTO findByID(long id);
	
	/**
	 * 조회 by email
	 * @param email
	 * @return
	 */
	MemberDTO findByEmail(String email);
	
	/**
	 * 회원존재우무 체크
	 * @param email
	 * @return
	 */
	boolean isExistEmail(String email);
	
	/**
	 * 로그인 체크
	 * @param id
	 * @param pw
	 * @return
	 */
	boolean isLogin(String email, String pw);
	
	List<MemberDTO> selectAll();
	
	/**
	 * 수정
	 * @param id
	 * @param memberDTO
	 */
	void update(long id, MemberDTO memberDTO);
	
	/**
	 * 삭제
	 * @param id
	 */
	void delete(long id);
	/**
	 * 삭제
	 * @param email
	 */
	void delete(String email);
	
	/**
	 * 이메일 찾기
	 * @param tel
	 * @param birth
	 * @return 이메일
	 */
	String findEmail(String tel, Date birth);
	
	/**
	 * 비밀번호 찾기
	 * @param email
	 * @param tel
	 * @param birth
	 * @return
	 */
	String findPw(String email,String tel,Date birth);

}
