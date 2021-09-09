package com.example.myprj.domain.common.dao;

import java.util.List;

import com.example.myprj.domain.common.dto.UpLoadFileDTO;

public interface UpLoadFileDAO {
	
	/**
	 * 업로드 파일추가
	 * @param list
	 */
	void addFile(UpLoadFileDTO uploadFileDTO);
	void addFiles(List<UpLoadFileDTO> list);
	
	/**
	 * 업로드 파일 조회
	 * @param rid
	 * @return
	 */
	List<UpLoadFileDTO> getFiles(String rid);
	List<UpLoadFileDTO> getFiles(String rid,String code);
	UpLoadFileDTO getFileByFid(String fid);
	UpLoadFileDTO getFileBySfname(String sfname);
	
	/**
	 * 업로드파일 삭제 by rid
	 * @param pid
	 */
	void deleteFileByRid(String rid);
	void deleteFileByRid(String rid,String code);
	
	/**
	 * 참조번호로 첨부파일 가져오기
	 * @param rid
	 * @return
	 */
	List<String> getStore_Fname(String rid);
	
	/**
	 * 업로드파일 삭제 by FID
	 * @param fid
	 */
	void deleteFileByFid(String fid);
	
	/**
	 * 업로드파일 삭제 by FID
	 * @param fid
	 */
	void deleteFileBySfname(String sfname);
}
