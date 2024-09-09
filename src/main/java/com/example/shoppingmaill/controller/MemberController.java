package com.example.shoppingmaill.controller;

import com.example.shoppingmaill.dto.MemberFormDto;
import com.example.shoppingmaill.entity.Member;
import com.example.shoppingmaill.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    // 회원가입 페이지
    // 회원가입 페이지를 요청할 경우 MemberFormDto 객체를 같이 넘김
    @GetMapping(value = "/new")
    public String memberForm(Model model){
        model.addAttribute("MemberFormDto", new MemberFormDto());
        return "member/memberForm";
    }


    // 회원가입
    // Post 요청으로 넘어온 회원가입 정보를 MemberFormDto 객체로 받음
    // 검증 하려는 객체(MemberFormDto) 앞에 @valid 어노테이션 지정
    @PostMapping(value = "/new")
    public String memberJoin(@Valid @ModelAttribute("MemberFormDto") MemberFormDto memberFormDto,
                             BindingResult bindingResult, Model model){

        // 유효성 검증에서 에러가 발생하면 회원가입 폼 페이지로 돌아갑니다.
        if (bindingResult.hasErrors()){
            return "member/memberForm";
        }

        try {
            // 유효성 검증에 통과하면, 새로운 회원 객첼르 생성하고 저장
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        }catch (IllegalStateException e){
            // 회원가입 과정에서 문제가 발생하면 에러 메시지를 모델에 추가,
            // 회원가입 폼으로 되돌아감
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberForm";
        }

        // 문제가 없으면 메인페이지로 redirect
        return "redirect:/";
    }

    // 로그인 페이지
    @GetMapping("/login")
    public String memberLogin(){
        return "member/loginForm";
    }

    // 로그인 실패
    @GetMapping("/login/fail")
    public String memberLoginFail(Model model){
        model.addAttribute("loginFailMsg","아이디 또는 비밀번호를 확인해주세요.");
        return "member/loginForm";
    }
}

