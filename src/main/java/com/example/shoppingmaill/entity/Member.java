package com.example.shoppingmaill.entity;

import com.example.shoppingmaill.constant.Role;
import com.example.shoppingmaill.dto.MemberFormDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;


@Entity
@Table(name="member")
@Getter
@Setter
@ToString
public class Member extends BaseEntity{

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;
    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;

    // Member 객체를 생성하기 위해 Member 객체 안에 createMember() static 메소드 생성
    public static Member createMember(MemberFormDto memberFormDto,
                                      PasswordEncoder passwordEncoder){
        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setAddress(memberFormDto.getAddress());
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);
        member.setRole(Role.ADMIN);
        return member;
    }


}
