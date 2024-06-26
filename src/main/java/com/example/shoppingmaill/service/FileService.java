package com.example.shoppingmaill.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;


// 이미지 파일 업로드
// UUID(Universally Unique IDentifier) - 서로 다른 개체들을 구별하기 위한 클래스
// FileOutputStream 클래스를 이용하여 파일을 저장
@Service
@Log
public class FileService {
    // 상품 이미지가 존재한다면 fileService.uploadFile() 메소드 수행
    // 이때 파라미터는 "저장위치", "원래 파일명", 이미지 Byte 파일"
    public String uploadFile(String uploadPath, String originalFileName,
                             byte[] fileData) throws IOException{
        // UUID를 이용하여 파일명 새로 생성
        // FileOutputStream을 이용하여 저장
        UUID uuid = UUID.randomUUID();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String savedFileName = uuid.toString() + extension; // 파일명

        // 경로 + 파일명
        String fileUploadFullUrl = uploadPath + "/" + savedFileName;

        // FileOutputStream 객체를 이용하여 경로지정 후 파일 저장
        FileOutputStream fos = new FileOutputStream((fileUploadFullUrl));
        fos.write(fileData);
        fos.close();

        return savedFileName;
    }

    public void deleteFile(String filePath){
        File deleteFile = new File(filePath);

        if(deleteFile.exists()) {
            deleteFile.delete();
            log.info("파일을 삭제하였습니다.");
        } else {
            log.info("파일을 존재하지 않습니다.");
        }
    }
}
