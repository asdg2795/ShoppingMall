package com.example.shoppingmaill.service;

import ch.qos.logback.core.util.StringUtil;
import com.example.shoppingmaill.entity.ItemImg;
import com.example.shoppingmaill.repository.ItemImgRepository;
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
    }
}
