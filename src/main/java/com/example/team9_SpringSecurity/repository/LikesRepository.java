package com.example.team9_SpringSecurity.repository;

import com.example.team9_SpringSecurity.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    Optional<Long> countByMemo_MemoIdAndReply_ReplyIdIsNull(Long memoId);
    Optional<Likes> findByMemo_MemoIdAndReply_ReplyIdIsNullAndUser_Id(Long memoId, Long userId);
    Optional<Likes> findByReply_ReplyIdAndUser_Id(Long replyId, Long userId);

//    @Query("select (l) from Likes l where (l.memo_id = :memoId) and (l.reply_id is null ") and (l.usern)
//    Optional<Long> findByMemo_MemoIdAndReply_ReplyIdIsNullAndUser_Username(@Param("memoId") Long memoId, String username);
    Optional<Long> countByReply_ReplyId(Long replyId);

}
