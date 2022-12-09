package com.example.team9_SpringSecurity.repository;

import com.example.team9_SpringSecurity.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReplyRepository extends JpaRepository<Reply,Long> {
    Optional<Reply> findByMemo_MemoIdAndReplyId(Long id, Long replyId);
    void deleteByReplyId(Long replyId);
}
