package com.example.shoppingmaill.dto;

import com.example.shoppingmaill.entity.ItemImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class ItemImgDto {
    // 상품을 등록 및 조회할 대 지정된 필드 뿐 아니라 추가적인 데이터들의 이동이 많으므로 여러 DTO 이용
    private Long id;
    private String imgName;
    private String oriImgName;
    private String imgUrl;
    private String repImgYn;

    private static ModelMapper modelMapper = new ModelMapper();

    public static ItemImgDto of(ItemImg itemImg) {
        return modelMapper.map(itemImg, ItemImgDto.class);
    }


}
