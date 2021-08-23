package com.example.myprj.domain.member.svc;

import org.springframework.stereotype.Service;

import com.example.myprj.domain.member.dao.MemberDAO;
import com.example.myprj.domain.member.dto.MemberDTO;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class MemberSVCImpl implements MemberSVC{

	private final MemberDAO memberDAO;
	
	@Override
	public void join(MemberDTO memberDTO) {
		long id = memberDAO.insert(memberDTO);
	}

	@Override
	public void update(long id, MemberDTO memberDTO) {
		memberDAO.update(id, memberDTO);
	}

}
