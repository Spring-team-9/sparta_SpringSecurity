package com.example.team9_SpringSecurity.repository;

import com.example.team9_SpringSecurity.entity.MemoLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemoLikeRepository extends JpaRepository<MemoLike, Long> {

    Optional<Long> countByMemo_MemoId(Long memoId);
    Optional<MemoLike> findByMemo_MemoIdAndUser_Id(Long memoId, Long userId);
}
