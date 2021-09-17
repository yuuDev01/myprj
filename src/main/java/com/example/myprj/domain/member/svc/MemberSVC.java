package com.example.myprj.domain.member.svc;

import java.sql.Date;

import com.example.myprj.domain.member.dto.MemberDTO;

public interface MemberSVC {
	/**
	 * 가입
	 * @param memberDTO
	 * @return
	 */
	void join(MemberDTO memberDTO);
		
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
	MemberDTO isLogin(String email, String pw);
	
	/**
	 * 회원 유무체크
	 * @param email
	 * @param pw
	 */
	boolean isMemember(String email, String pw);
	
	/**
	 * 수정
	 * @param id
	 * @param memberDTO
	 */
	void update(long id, MemberDTO memberDTO);
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
	
	
	/**
	 * id로 회원 삭제
	 * @param id
	 */
	void delete(long id);
	/**
	 * email로 회원 삭제
	 * @param email
	 */
	void delete(String email);	
	/**
	 * 탈퇴
	 * @param email
	 */
	void outMember(String email, String pw);	
	
	/**
	 * 회원 비밀번호 변경
	 * @param prePw
	 * @param postPw
	 */
	boolean changePw(String email, String prePw, String postPw);	
	
	/**
	 * 별칭 변경
	 * @param email
	 * @param nickname
	 */
	boolean changeNickname(Long id, String nickname);
}
