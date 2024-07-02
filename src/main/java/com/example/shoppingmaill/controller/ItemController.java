package com.example.shoppingmaill.controller;

import com.example.shoppingmaill.dto.ItemFormDto;
import com.example.shoppingmaill.dto.ItemSearchDto;
import com.example.shoppingmaill.entity.Item;
import com.example.shoppingmaill.service.ItemService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

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

    // 상품 수정
    // 수정① "ADMIN" 권한을 가진 아이디로 상품 수정 페이지 GET 요청
    @GetMapping(value = "/admin/item/{itemId}")
    public String itemDetail(@PathVariable(name = "itemId") Long itemId, Model model){

        try{
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
            // 수정② 수행하여 해당 상품 조회
            model.addAttribute("ItemFormDto", itemFormDto);
            // 수정③ 상품 수정 페이지 반환하면서 해당 상품 DTO 객체로 넘김
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "존재하지 않는 상품입니다.");
            model.addAttribute("itemFormDto", new ItemFormDto());
            return "item/itemForm";
        }
        return "item/itemForm";
    }

    // 수정④ 상품 수정 페이지에서 수정한 후 "수정" (POST 요청)
    @PostMapping(value = "/admin/item/{itemId}")
    public String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, Model model,
                             @RequestParam(name = "itemImgFile") List<MultipartFile> itemImgFileList) {

        if (bindingResult.hasErrors()) {
            return "item/itemForm";
        }

        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage",
                    "첫번째 심플 이미지는 필수 입력 값 입니다.");
            return "item/itemForm";
        }

        try {
            itemService.updateItem(itemFormDto, itemImgFileList);
            // 수정⑤ 입력값을 검증하고 itemService.updateItem() 메소드를 수행
            // 파라미터는 입력받은 ItemFormDto 객체와 이미지 정보를 담고있는 itemImgFileList를 넘김
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }

        return "redirect:/";
    }

    // 요청 URL에 페이지 번호가 없는 경우와 있는 경우 2가지를 매핑
    @GetMapping(value = {"/admin/items", "/admin/items/{page}"})
    public String itemManage(ItemSearchDto itemSearchDto,
                             @PathVariable("page") Optional<Integer> page,
                             Model model){
        // PageRequest.of() 를 통해서 Pageable 객체 생성
        // 첫번째 파라미터는 조회할 페이지 번호, 두번째는 한 번에 가져올 데이터 수
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);
        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);
        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        // View 단에서 하단에 보여줄 페이지 번호의 최대 개수 설정
        model.addAttribute("maxPage", 5);
        return "item/itemMng";
    }

    @GetMapping(value = "/item/{itemId}")
    public String itemDetail(Model model, @PathVariable(name = "itemId") Long itemId){
        ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
        model.addAttribute("Item", itemFormDto);
        return "item/itemDetail";
    }
}
