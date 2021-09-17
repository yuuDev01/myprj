package com.example.myprj.domain.member.svc;

import java.sql.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.example.myprj.domain.common.dao.UpLoadFileDAO;
import com.example.myprj.domain.member.dao.MemberDAO;
import com.example.myprj.domain.member.dto.MemberDTO;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
//@Transactional(readOnly=true) //1.트랜잭성 보장 2.서비스층에서 사용
public class MemberSVCImpl implements MemberSVC{

	private final MemberDAO memberDAO;
	private final UpLoadFileDAO upLoadFileDAO;
	
	//가입
	@Override
	@org.springframework.transaction.annotation.Transactional(readOnly = false)
	public void join(MemberDTO memberDTO) {
		long id = memberDAO.insert(memberDTO);

		//취미 정보가 있으면
		List<String> hobby = memberDTO.getHobby();
		if( hobby != null && hobby.size() > 0) {
			memberDAO.addHobby(id,memberDTO.getHobby());
		}		
	}

	@Override
	public MemberDTO findByEmail(String email) {
		//회원정보 가져오기
		MemberDTO memberDTO = memberDAO.findByEmail(email);
		//회원의 취미 가져오기
		memberDTO.setHobby(memberDAO.getHobby(memberDTO.getId()));
		//회원 이미지 정보 가져오기
		memberDTO.setFile(upLoadFileDAO.getFileByRid(String.valueOf(memberDTO.getId())));		
		return memberDTO;
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
	
	//회원 우무체크
	@Override
	public boolean isMemember(String email, String pw) {
		return memberDAO.isLogin(email, pw);
	}
	
	//회원 수정
	@Override
	//@Transactional(readOnly = false)
	public void update(long id, MemberDTO memberDTO) {
		//회원수정
		memberDAO.update(id, memberDTO);
		//취미수정
		memberDAO.delHobby(id);
		memberDAO.addHobby(id,memberDTO.getHobby());
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
	
	//email로 회원 탈퇴
	@Override
	public void outMember(String email, String pw) {
		memberDAO.outMember(email, pw);
	}
	
	//비밀번호 변경
	@Override
	public boolean changePw(String email, String prePw, String postPw) {
//		boolean isChanged = false;
//		if(memberDAO.changePw(prePw, postPw)==1) isChanged = true;		 
//		return isChanged;
		
		return memberDAO.changePw(email, prePw, postPw) == 1 ? true : false;
	}
	
	//별칭 변경
	@Override
	public boolean changeNickname(Long id, String nickname) {
		
		return memberDAO.changeNickname(id, nickname) == 1 ? true : false;
	}
}
