package com.example.shoppingmaill.service;

import com.example.shoppingmaill.dto.ItemFormDto;
import com.example.shoppingmaill.entity.Item;
import com.example.shoppingmaill.entity.ItemImg;
import com.example.shoppingmaill.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository; // 상품 저장
    private final ItemImgService itemImgService; // 상품 이미지 저장

    public Long saveItem(ItemFormDto itemFormDto,
                         List<MultipartFile> itemImgFileList) throws Exception { // 파라미터는 입력받은 itemFormDto 객체와 이미지 정보를 담고 있는 itemImgFileList를 넘김
        // 상품 등록
        Item item = itemFormDto.createItem();
        itemRepository.save(item);// itemFormDto 객체를 item 엔티티로 변환하고 메소드 수행

        // 이미지 등록
        for(int i = 0; i < itemImgFileList.size(); i++){
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);
            if (i == 0) {
                itemImg.setRepImgYn("Y");
            } else {
                itemImg.setRepImgYn("N");
            }
            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
            // itemImg 객체를 생성하고 itemImgService.saveItemImg() 메소드 수행
            // 파라미터는 itemImg 객체와 이미지 정보를 담고 있는 itemImgFileList.get(i) 객체 하나를 지정
            // ㅑ
        }
        return item.getId();
    }
}
