package com.example.shoppingmaill.controller;

import com.example.shoppingmaill.dto.ItemFormDto;
import com.example.shoppingmaill.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    // 상품 등록 페이지 접근
    @GetMapping(value = "/admin/item/new")
    public String itemForm(Model model){
        model.addAttribute("ItemFormDto", new ItemFormDto());
        return "item/itemForm";
    }

    // 상품 등록(상품 정보 및 이미지 입력하고 저장)
    @PostMapping(value = "/admin/item/new")
    public String itemNew(@Valid @ModelAttribute("ItemFormDto") ItemFormDto itemFormDto,
                          BindingResult bindingResult,
                          Model model,
                          @RequestParam(name="itemImgFile") List<MultipartFile> itemImgFileList){

        if (bindingResult.hasErrors()){
            return "item/itemForm";
        }

        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage",
                    "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "item/itemForm";
        }

        try{
            itemService.saveItem(itemFormDto, itemImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage",
                    "상품 등록 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }
        return "redirect:/";
    }
}
