package com.example.shoppingmaill.service;

import com.example.shoppingmaill.entity.Member;
import com.example.shoppingmaill.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;


    // 회원가입
    public Member saveMember(Member member){
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    // 중복회원 검증
    public void validateDuplicateMember(Member member){
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if(findMember != null){
            throw new IllegalStateException("이미 가입된 회원 입니다.");
        }
    }

    // 로그인 요청 검증을 위한 User 객체
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email); // 사용자 조회

        if(member == null){ // 사용자 조회시 없을 경우 예외 발생
            throw new UsernameNotFoundException(email);
        }

        return User.builder()
                //Builder 패턴을 이용하여 UserDetail 인터페이스를 구현한 User 객체 생성 후 반환
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }
}
