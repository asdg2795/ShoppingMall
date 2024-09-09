package com.example.shoppingmaill.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

// 이미지 파일 저장 로직을 담당할 Service 객체
// 파일 저장은 DB 에 저장되는 것이 아니기 때문에 Repository 필요 없음(FileOutputStream 가 대신함)
// 이미지 파일 업로드
// UUID(Universally Unique IDentifier) - 서로 다른 개체들을 구별하기 위한 클래스
// FileOutputStream 클래스를 이용하여 파일을 저장
@Service
@Log
public class FileService {
    public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception{
        UUID uuid = UUID.randomUUID();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String savedFileName = uuid.toString() + extension;

        String fileUploadFullUrl = uploadPath +"+"+ savedFileName;

        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
        fos.write(fileData);
        fos.close();

        return savedFileName;
    }

    public void deleteFile(String fileData) throws Exception{
        File deleteFile = new File(fileData);

        if(deleteFile.exists()){
            deleteFile.delete();
            log.info("파일을 삭제하였습니다.");
        }else{
            log.info("파일이 존재하지 않습니다.");
        }
    }
}
