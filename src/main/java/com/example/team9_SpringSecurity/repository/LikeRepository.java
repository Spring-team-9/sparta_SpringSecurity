package com.example.team9_SpringSecurity.repository;

import com.example.team9_SpringSecurity.entity.LikeMemo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<LikeMemo, Long> {
    Optional<LikeMemo> findByMemoMemoId(Long id);
    void deleteByMemoMemoId(Long id);
}
