package com.example.myprj.domain.member.svc;

import java.sql.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.example.myprj.domain.member.dao.MemberDAO;
import com.example.myprj.domain.member.dto.MemberDTO;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
@Transactional //1.트랜잭성 보장 2.서비스층에서 사용
public class MemberSVCImpl implements MemberSVC{

private final MemberDAO memberDAO;
	
	//가입
	@Override
	public void join(MemberDTO memberDTO) {
		long id = memberDAO.insert(memberDTO);

		//취미 정보가 있으면
		List<String> hobby = memberDTO.getHobby();
		if( hobby != null && hobby.size() > 0) {
			memberDAO.addHobby(id,memberDTO.getHobby());
		}		
	}

	//이메일 중복체크
	@Override
	public boolean isExistEmail(String email) {
		
		return memberDAO.isExistEmail(email);
	}
	
	//로그인 체크
	@Override
	public MemberDTO isLogin(String email, String pw) {
		MemberDTO mdto = null;
		if(memberDAO.isLogin(email, pw)) {
			mdto = memberDAO.findByEmail(email);
		}
		return mdto;
	}
	
	@Override
	public void update(long id, MemberDTO memberDTO) {
		memberDAO.update(id, memberDTO);
	}

	//이메일 찾기
	@Override
	public String findEmail(String tel, Date birth) {
		
		return memberDAO.findEmail(tel, birth);
	}
	
	//비밀번호 찾기
	@Override
	public String findPw(String email, String tel, Date birth) {
	
		return memberDAO.findPw(email, tel, birth);
	}
	
	//id로 회원 삭제
	@Override
	public void delete(long id) {
		memberDAO.delete(id);
	}
	
	//email로 회원 삭제
	@Override
	public void delete(String email) {
		memberDAO.delete(email);
	}

}
