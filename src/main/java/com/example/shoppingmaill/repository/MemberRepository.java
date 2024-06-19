package com.example.shoppingmaill.repository;

import com.example.shoppingmaill.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByEmail(String email);
}
