package com.example.team9_SpringSecurity.repository;

import com.example.team9_SpringSecurity.entity.LikeMemo;
import com.example.team9_SpringSecurity.entity.LikeReply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeReplyRepository extends JpaRepository<LikeReply, Long>  {

    Optional<LikeMemo> findByMemoMemoId(Long id);
    void deleteByReplyReplyId(Long replyId);
}
