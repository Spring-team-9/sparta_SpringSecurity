package com.example.team9_SpringSecurity.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter                 // Class 모든 필드의 Getter method를 생성
@MappedSuperclass       // 공통 매핑 정보가 필요할 때, 부모클래스에 선언하고 속성만 상속 받아서 사용 하고 싶을때 사용
// @EntityListeners : 삽입, 삭제, 수정, 조회 등의 작업을 할 때 전, 후에 어떠한 작업을 하기 위해 이벤트 처리를 위한 어노테이션
@EntityListeners(AuditingEntityListener.class) // AuditingEntityListener.class : Audit(감시하다) 옵션은 시간에 대해서 자동으로 값을 넣어 주는 기능
public class Timestamped {

    @CreatedDate        // 생성된 시간 정보
    private LocalDateTime createdAt;

    @LastModifiedDate   // 수정된 시간 정보
    private LocalDateTime modifiedAt;
}
