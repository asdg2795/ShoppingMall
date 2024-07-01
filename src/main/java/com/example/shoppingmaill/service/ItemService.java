package com.example.shoppingmaill.service;

import com.example.shoppingmaill.dto.ItemFormDto;
import com.example.shoppingmaill.dto.ItemImgDto;
import com.example.shoppingmaill.dto.ItemSearchDto;
import com.example.shoppingmaill.entity.Item;
import com.example.shoppingmaill.entity.ItemImg;
import com.example.shoppingmaill.repository.ItemImgRepository;
import com.example.shoppingmaill.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository; // 상품 저장
    private final ItemImgService itemImgService; // 상품 이미지 저장
    private final ItemImgRepository itemImgRepository;

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
        }
        return item.getId();
    }

    // 상품 조회
    @Transactional(readOnly = true)
    // DB에서 객체를 조회할 때, 읽기 전용 상태로 설정하는 어노테이션과 속성
    public ItemFormDto getItemDtl(Long itemId){

        // 상품 id를 기반으로 상품 이미지 엔티티 객체 가져옴
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);

        // 상품 이미지 DTO 객체 담을 그릇 생성
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();

        // 상품 이미지 엔티티 객체를 상품 이미지 DTO 객체를 변환
        for(ItemImg itemImg : itemImgList){
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }

        // 상품 id를 기반으로 상품 엔티티 객체 가져옴
        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);

        // 상품 엔티티 객체를 상품 DTO 객체로 변환
        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        return itemFormDto;

    }

    // 상품 수정
    public Long updateItem(ItemFormDto itemFormDto,
                           List<MultipartFile> itemImgFileList) throws Exception{
       // 상품 수정
       Item item = itemRepository.findById(itemFormDto.getId())
               .orElseThrow(EntityNotFoundException::new);
       item.updateItem(itemFormDto);
       // 수정⑥ Item 객체를 조회하고 item.updateItem(itemFormDto) 수행

       // 상품 이미지 수정
       List<Long> itemImgIds = itemFormDto.getItemImgIds();
       for(int i = 0; i < itemImgFileList.size(); i++){
           itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
           // 수정⑦ 메소드 수행, 이때) 파라미터는 "상품 이미지의 id", "상품 이미지 파일"
       }
       return item.getId();
    }

    // 조회 기능
    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        return itemRepository.getAdminItemPage(itemSearchDto, pageable);
    }
}
