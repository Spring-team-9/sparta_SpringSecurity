package com.example.team9_SpringSecurity.repository;

import com.example.team9_SpringSecurity.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long> {

    public List<Memo> findAllByOrderByCreatedAtDesc();         // findAllBy(모두찾는다)/OrderBy(-를 기준으로 정렬로)/ModifiedAt(ModifiedAt멤버변수를)/Desc(내림차순)
}
