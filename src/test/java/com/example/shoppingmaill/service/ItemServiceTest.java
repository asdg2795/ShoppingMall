package com.example.shoppingmaill.service;

import com.example.shoppingmaill.constant.ItemSellStatus;
import com.example.shoppingmaill.dto.ItemFormDto;
import com.example.shoppingmaill.entity.Item;
import com.example.shoppingmaill.entity.ItemImg;
import com.example.shoppingmaill.repository.ItemImgRepository;
import com.example.shoppingmaill.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.yaml")
class ItemServiceTest {

    @Autowired
    ItemService itemService;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemImgRepository itemImgRepository;

    List<MultipartFile> createMultipartFiles() throws Exception{
        List<MultipartFile> multipartFileList = new ArrayList<>();

        for(int i = 0; i < 5; i++){
            String path = "/Users/mangez_js/study/item";
            String imgName = "image" + i +".jpg";
            MockMultipartFile multipartFile = new MockMultipartFile(path, imgName,
                    "image/jpg", new byte[]{1,2,3,4});
            multipartFileList.add(multipartFile);
        }
        return multipartFileList;
    }

    @Test
    @DisplayName("상품 등록 테스트")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void saveItem() throws Exception {
        ItemFormDto itemFormDto = new ItemFormDto();
        itemFormDto.setItemNm("테스트 상품");
        itemFormDto.setItemSellStatus(ItemSellStatus.SELL);
        itemFormDto.setItemDetail("테스트 상품 입니다.");
        itemFormDto.setPrice(1000);
        itemFormDto.setStockNumber(100);

        List<MultipartFile> multipartFileList = createMultipartFiles();
        Long itemId = itemService.saveItem(itemFormDto, multipartFileList);
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);

        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);

        assertEquals(itemFormDto.getItemNm(), item.getItemNm());
        assertEquals(itemFormDto.getItemSellStatus(), item.getItemSellStatus());
        assertEquals(itemFormDto.getItemDetail(), item.getItemDetail());
        assertEquals(itemFormDto.getPrice(), item.getPrice());
        assertEquals(itemFormDto.getStockNumber(), item.getStockNumber());

        String originalFileName = multipartFileList.get(0).getOriginalFilename();
        String savedFileName = itemImgList.get(0).getOriImgName();

        // 파일 확장자 확인
        assertEquals(originalFileName.substring(originalFileName.lastIndexOf(".")), savedFileName.substring(savedFileName.lastIndexOf(".")));

        // 원래 파일 이름을 그대로 저장하는지 확인하려면 아래 줄을 주석처리
        // assertEquals(originalFileName, savedFileName);
    }

}