package com.example.shoppingmaill.service;

import ch.qos.logback.core.util.StringUtil;
import com.example.shoppingmaill.entity.ItemImg;
import com.example.shoppingmaill.repository.ItemImgRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {// 상품 이미지 업로드, 상품 이미지 정보 저장 Service

    @Value("${itemImgLocation}")
    private String itemImgLocation;

    private final ItemImgRepository itemImgRepository; // 상품 이미지 정보 저장
    private final FileService fileService; // 상품 이미지 업로드

    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgfile) throws Exception {
        String oriImgName = itemImgfile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        // 파일 업로드
        if (!StringUtils.isEmpty(oriImgName)) {
            imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgfile.getBytes());
            imgUrl = "/images/item/" + imgName;
        }

        // 상품 이미지 정보 저장
        itemImg.updateItemImg(oriImgName, imgName, imgUrl);
        itemImgRepository.save(itemImg);
        // 메소드 수행
    }

    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception{
        // 상품 이미지를 수정했다면, 기존의 이미지 정보 객체를 불러옴
        if(!itemImgFile.isEmpty()){
            ItemImg savedImgItem = itemImgRepository.findById(itemImgId)
                    .orElseThrow(EntityNotFoundException::new);
            // 기존 이미지 파일이 존재한다면 삭제 (존재하지 않는다면 이미지를 추가한 것)
            if(!StringUtils.isEmpty(savedImgItem.getImgName())){
                fileService.deleteFile(itemImgLocation+"/"+savedImgItem.getImgName());
            }

            String oriImgName = itemImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(itemImgLocation,
                    oriImgName,itemImgFile.getBytes());
            String imgUrl = "/images/item/" + imgName;
            savedImgItem.updateItemImg(oriImgName,imgName,imgUrl);
            // 수정⑧ 기존 이미지 파일 삭제 후 savedItemImg.updateItemImg() 수행
        }
    }
}

