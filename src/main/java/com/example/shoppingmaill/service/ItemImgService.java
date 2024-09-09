package com.example.shoppingmaill.service;

import ch.qos.logback.core.util.StringUtil;
import com.example.shoppingmaill.dto.ItemFormDto;
import com.example.shoppingmaill.dto.ItemImgDto;
import com.example.shoppingmaill.entity.Item;
import com.example.shoppingmaill.entity.ItemImg;
import com.example.shoppingmaill.repository.ItemImgRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.io.File;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {// 상품 이미지 업로드, 상품 이미지 정보 저장 Service
    @Value("${itemImgLocation}")
    private String itemImgLocation;

    private final ItemImgRepository itemImgRepository;
    private final FileService fileService;

    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception{
        String oriImgName = itemImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        if(!StringUtils.isEmpty(oriImgName)){
            imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            imgUrl = itemImgFile + imgName;
        }

        itemImg.updateItemImg(oriImgName, imgName, imgUrl);
        itemImgRepository.save(itemImg);
    }

   public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception{
        if(!itemImgFile.isEmpty()){
            ItemImg savedItemImg = itemImgRepository.findById(itemImgId).orElseThrow(EntityNotFoundException::new);

            if(!StringUtils.isEmpty(itemImgFile.getContentType())){
                fileService.deleteFile(itemImgLocation+"/"+savedItemImg);
            }

            String oriImgName = itemImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            String imgUrl = "/item/images/" + imgName;
            savedItemImg.updateItemImg(oriImgName, imgUrl, imgName);
        }
   }


}

