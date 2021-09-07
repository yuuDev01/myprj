package com.example.myprj.domain.common.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UpLoadFileDTO {
  private long fid; 						//파일아이디 fid number(10),
  private String rid; 					//참조아이디 rid varchar2(10),
  private String code;					//코드 code varchar2(11),
  private String store_fname; 	//서버보관 파일명 store_fname varchar2(50),
  private String upload_fname; 	//업로드 파일명 upload_fname varchar2(50),
  private String fsize;					//파일 크기 fsize varchar2(45),
  private String ftype;					//파일 유형 ftype varchar2(50),
  private LocalDateTime cdate; 	//생성일시 timestamp default systimestamp,
  private LocalDateTime udate; 	// 수정일시 udate timestamp,
}