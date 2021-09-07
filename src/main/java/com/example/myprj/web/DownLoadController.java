package com.example.myprj.web;

import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.UriUtils;

import com.example.myprj.domain.common.file.FileStore;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class DownLoadController {

   private final FileStore fileStore;
   
   /**
    * 이미지 다운로드
    * @param fname
    * @return
    * @throws MalformedURLException
    */
   @ResponseBody //http응답 메세지 바디에 직접내용쓰기
//   @ResponseStatus(.ok)//응답코드 200
   @GetMapping("/images/{fname}")
   public Resource downLoadImage(@PathVariable String fname) throws MalformedURLException  {
      Resource resource = new UrlResource("file:"+fileStore.getFullPath(fname));
      return resource;
   }
   
   /**
    * 첨부파일 다운로드
    * @param sfname
    * @param ufname
    * @return
    * @throws MalformedURLException
    */
   @GetMapping("/attach/{sfname}/{ufname}")
   public ResponseEntity<Resource> downloadAttach(
         @PathVariable String sfname,
         @PathVariable String ufname) throws MalformedURLException{
      
      Resource resource = new UrlResource("file:"+fileStore.getFullPath(sfname));
      
      //한글파일명 깨짐 방지를위한 인코딩
      String encodeUploadFileName = UriUtils.encode(ufname, StandardCharsets.UTF_8);
      //Http응답 메세지 헤더에 첨부파일이 있음을 알림
      String contentDisposition = "attachment; filename="+ encodeUploadFileName;
      
      return ResponseEntity.ok()  //응답코드 200
                                     .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                                     .body(resource);
   }
}
