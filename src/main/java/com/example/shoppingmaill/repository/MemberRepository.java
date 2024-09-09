package com.example.shoppingmaill.repository;

import com.example.shoppingmaill.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberRepository extends JpaRepository<Member, Long> {
    // 회원가입 시 중복 여부를 판단하기 위해서 이메일로 회원을 검사하도록 쿼리 메소드 작성
    Member findByEmail(String email);
}
