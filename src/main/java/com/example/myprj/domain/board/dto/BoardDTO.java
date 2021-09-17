package com.example.myprj.domain.board.dto;

import java.sql.Timestamp;
import java.util.List;

import com.example.myprj.domain.common.dto.UpLoadFileDTO;

import lombok.Data;

@Data
public class BoardDTO {
	private Long bnum; // BNUM NUMBER(10,0)
	private String bcategory; // BCATEGORY VARCHAR2(11 BYTE)
	private String btitle; // BTITLE VARCHAR2(150 BYTE)
	private Long bid; // BID NUMBER(8,0)
	private String bemail; // BEMAIL VARCHAR2(40 BYTE)
	private String bnickname; // BNICKNAME VARCHAR2(30 BYTE)
	private Long bhit; // BHIT NUMBER(5,0)
	private String bcontent; // BCONTENT CLOB
	private Long pbnum; // PBNUM NUMBER(10,0)
	private Long bgroup; // BGROUP NUMBER(10,0)
	private Long bstep; // BSTEP NUMBER(3,0)
	private Long bindent; // BINDENT NUMBER(3,0)
	private String status; // STATUS CHAR(1 BYTE)
	private Timestamp bcdate; // BCDATE TIMESTAMP(6)
	private Timestamp budate; // BUDATE TIMESTAMP(6)
	private List<UpLoadFileDTO> files;
}