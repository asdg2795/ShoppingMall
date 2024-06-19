package com.example.shoppingmaill.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

// 등록자 및 수정자, 등록 및 수정 시간을 모두 갖는 Entity
@EntityListeners(value={AuditingEntityListener.class})
@MappedSuperclass
@Getter @Setter
public abstract class BaseEntity extends BaseTimeEntity{
    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    @LastModifiedBy
    private String modifiedBy;
}
