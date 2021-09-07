package com.example.myprj.domain.common.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.example.myprj.domain.common.dto.MetaOfUploadFile;

/**
 * 업로드 파일을 파일시스템에 저장후 
 * 업로드 파일에서 추출한 메타정보를 UpLoadFile객체에 저장
 * @author mypc
 *
 */
@Component
public class FileStore {

	//첨부파일이 저장될 파일시스템의 경로 application.properties에서 정의
	@Value("${bbs.file.path}")
	private String filePath;
	
	public String getFullPath(String fileName) {
		return filePath + fileName;
	}
	
	/**
	 * 멀티파트파일 여러개 저장
	 * @param multipartFiles
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public List<MetaOfUploadFile
	> storeFiles(List<MultipartFile> multipartFiles) throws IllegalStateException, IOException{
		
		List<MetaOfUploadFile> storeFileResult = new ArrayList<>();
		
		for(MultipartFile mf : multipartFiles) {
			if(!mf.isEmpty()) {
				storeFileResult.add(storeFile(mf));
			}
		}
		
		return storeFileResult;
	}
	
	/**
	 * 멀티파트 파일 1개 저장
	 * @param multipartFile
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public MetaOfUploadFile storeFile(MultipartFile multipartFile) throws IllegalStateException, IOException {
		
		if(multipartFile.isEmpty()) return null;
		
		//클라이언트가 업로드한 파일명
		String originalFileName = multipartFile.getOriginalFilename();
		//서버에 저장할 파일명
		String storeFileName = createStoreFileName(originalFileName);
		String fsize = String.valueOf(multipartFile.getSize());
		String ftype = multipartFile.getContentType();
		
		//파일시스템에 저장
		multipartFile.transferTo(Path.of(getFullPath(storeFileName)));
		
		return new MetaOfUploadFile(storeFileName, originalFileName, fsize, ftype);
	}
	
	// 업로드파일명에서 확장자 추출
	private String extractExt(String originalFilename) {
		int posOfExt = originalFilename.lastIndexOf(".");
		String ext = originalFilename.substring(posOfExt+1);
		return ext;
	}
	
	// 서버보관파일명 생성
	private String createStoreFileName(String originalFilename) {		
		String storFilename = UUID.randomUUID().toString();
		return storFilename + "." + extractExt(originalFilename);
	}	
	
	/**
	 * 서버 보관 파일 삭제
	 * @param fname
	 * @return
	 */
	public boolean deleteFile(String fname) {
		
		boolean isDeleted = false;
		
		File file = new File(getFullPath(fname));
		
		if(file.exists()) {
			if(file.delete()) {
				isDeleted = true;
			}
		}
		
		return isDeleted;
	}
	
	public boolean deleteFiles(List<String> fnames) {
		
		boolean isDeleted = false;
		int deletedFileCount = 0;
		
		for(String fname : fnames) {
			if(deleteFile(fname)) {
				deletedFileCount++;
			};
		}
		
		if(deletedFileCount == fnames.size()) isDeleted = true;
		
		return isDeleted;
	}
}
