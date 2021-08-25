package com.example.myprj.domain.member.dto;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class MemberDTO {
	private long id;					// 내부관리 아이디  id        number(8),
	private String email;			//  email     varchar2(40),
	private String pw;				//  pw        varchar2(10) constraint member_pw_nn not null,
	private String tel;				//  tel       varchar2(13),
	private String nickname;	//  nickname  varchar2(30),--한글 3byte
	private String gender;		//  gender    char(3),
	private String region;		//  region    varchar2(30),
	private Date birth;	//  birth     date,
	private List<String> hobby;			//취미	
	private String letter;			//  letter    char(1),
	private Long fid;					//  fid       number(10),
	private	LocalDateTime cdate;//  cdate     timestamp default systimestamp,
	private LocalDateTime udate;//  udate     timestamp,
}