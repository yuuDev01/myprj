package com.example.myprj.domain.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 업로드한 파일의 메타정보
 * @author mypc
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetaOfUploadFile {
  private String store_fname; 	//서버관리를 위한 파일명(파일명 중복방지 UUID)
  private String upload_fname; 	//클라이언트가 업로드한 파일명
  private String fsize;			//파일크기
  private String ftype;			//파일유형		
}
