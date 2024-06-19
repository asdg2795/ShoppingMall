package com.example.shoppingmaill.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


@EntityListeners(value = {AuditingEntityListener.class})
// 기능을 이용해서 Entity 등록 및 수정 시에 Auditing 기능을 수행하는 원리
@MappedSuperclass
// 해당 클래스를 상속 받는 Entity들이 공통되는 필드를 사용할 수 있도록 부모 클래스에 지정
@Getter
@Setter
public abstract class BaseTimeEntity {

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime regTime;

    @LastModifiedDate
    private LocalDateTime updateTime;
}
