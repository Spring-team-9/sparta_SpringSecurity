package com.example.team9_SpringSecurity.service;

import com.example.team9_SpringSecurity.dto.*;
import com.example.team9_SpringSecurity.entity.*;
import com.example.team9_SpringSecurity.repository.MemoLikeRepository;
import com.example.team9_SpringSecurity.repository.MemoRepository;
import com.example.team9_SpringSecurity.repository.ReplyLikeRepository;
import com.example.team9_SpringSecurity.repository.ReplyRepository;
import com.example.team9_SpringSecurity.util.error.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.team9_SpringSecurity.util.error.ErrorCode.*;

@Service
@RequiredArgsConstructor        // 생성자 자동 주입
public class MemoService {

    private final MemoRepository memoRepository;
    private final ReplyRepository replyRepository;
    private final MemoLikeRepository memoLikeRepository;
    private final ReplyLikeRepository replyLikeRepository;


    // 전체 글 조회
    public MessageDto getMemos(){
        List<Memo> memolist = memoRepository.findAllByOrderByCreatedAtDesc();
        List<MemoResponseDto> responseDtoList = new ArrayList<>();

        for(Memo memo : memolist){
            MemoResponseDtoBuilder mrdBuilder = new MemoResponseDtoBuilder();
            MemoResponseDto responseDto =
                    mrdBuilder.id(memo.getMemoId())
                            .title(memo.getTitle())
                            .username(memo.getUsername())
                            .content(memo.getContent())
                            .likeCnt(cnt("Memo",memo.getMemoId()))
                            .createdAt(memo.getCreatedAt())
                            .modifiedAt(memo.getModifiedAt())
                            .addReply(addLikeCntToReplyResponseDto(memo.getReplies()))
                            .getMemos();

            responseDtoList.add(responseDto);
        }
        return new MessageDto(StatusEnum.OK, responseDtoList);
    }

    // 선택 글 조회 기능
    public MessageDto getMemos(Long id){
        Memo memo = memoRepository.findById(id).orElseThrow(
                () -> new CustomException(MEMO_NOT_FOUND)
        );

        MemoResponseDtoBuilder mrdBuilder = new MemoResponseDtoBuilder();
        MemoResponseDto responseDto =
                mrdBuilder.id(memo.getMemoId())
                        .title(memo.getTitle())
                        .username(memo.getUsername())
                        .content(memo.getContent())
                        .likeCnt(cnt("Memo",memo.getMemoId()))
                        .createdAt(memo.getCreatedAt())
                        .modifiedAt(memo.getModifiedAt())
                        .addReply(addLikeCntToReplyResponseDto(memo.getReplies()))
                        .getMemos();

        return new MessageDto(StatusEnum.OK, responseDto);
    }

    // 글 작성 기능
    public MessageDto createMemo(MemoRequestDto dto, User user){

        Memo memo = new Memo(dto, user);                        // 컨트롤러에서 @RequestBody 어노테이션으로 body의 내용을 가져온건데 또 할 필요 없겠지
        memoRepository.save(memo);

        MemoResponseDtoBuilder mrdBuilder = new MemoResponseDtoBuilder();
        MemoResponseDto responseDto =
                mrdBuilder.id(memo.getMemoId())
                        .title(memo.getTitle())
                        .username(memo.getUsername())
                        .content(memo.getContent())
                        .createdAt(memo.getCreatedAt())
                        .modifiedAt(memo.getModifiedAt())
                        .getMemos();

        return new MessageDto( StatusEnum.OK, responseDto);
    }

    // 글 수정 기능
    @Transactional
    public MessageDto modifyMemo (Long id, MemoRequestDto dto, User user) {
        Memo memo = memoRepository.findById(id).orElseThrow(
                () -> new CustomException(MEMO_NOT_FOUND)
        );

        if (memo.getUsername() == user.getUsername() || user.getRole() == UserRoleEnum.ADMIN) {
            memo.update(dto);  // update는 entity에 새로 정의한 함수
        } else {
            throw new CustomException(NO_ACCESS);
        }

        MemoResponseDtoBuilder mrdBuilder = new MemoResponseDtoBuilder();
        MemoResponseDto responseDto =
                mrdBuilder.id(memo.getMemoId())
                        .title(memo.getTitle())
                        .username(memo.getUsername())
                        .content(memo.getContent())
                        .likeCnt(cnt("Memo",memo.getMemoId()))
                        .createdAt(memo.getCreatedAt())
                        .modifiedAt(memo.getModifiedAt())
                        .addReply(addLikeCntToReplyResponseDto(memo.getReplies()))
                        .getMemos();

        return new MessageDto( StatusEnum.OK, responseDto);
    }


    // 글 삭제 기능
    @Transactional
    public MessageDto deleteMemo (Long id, User user) {
        Memo memo = memoRepository.findById(id).orElseThrow(
                () -> new CustomException(MEMO_NOT_FOUND)
        );

        if (memo.getUsername() == user.getUsername() || user.getRole() == UserRoleEnum.ADMIN) {
            memoRepository.deleteById(id);
        } else {
            throw new CustomException(NO_ACCESS);
        }


        return new MessageDto(StatusEnum.OK);
    }

    // 댓글 작성 기능
    public MessageDto createReply(Long id, ReplyRequestDto dto, User user) {
        Memo memo = memoRepository.findById(id).orElseThrow(
                () -> new CustomException(MEMO_NOT_FOUND)
        );

        Reply reply = new Reply (dto, user, memo);
        replyRepository.save(reply);
        ReplyResponseDto responseDto = new ReplyResponseDto(reply, cnt("Reply", reply.getReplyId()));
        return new MessageDto(StatusEnum.OK, responseDto);
    }

    // 댓글 수정 기능
    @Transactional
    public MessageDto modifyReply(Long id, Long replyId, ReplyRequestDto dto, User user) {
        Memo memo = memoRepository.findById(id).orElseThrow(
                () -> new CustomException(MEMO_NOT_FOUND)
        );

        Reply reply = replyRepository.findByMemo_MemoIdAndReplyId(id, replyId).orElseThrow(
                () -> new CustomException(REPLY_NOT_FOUND)
        );

        if (reply.getReplyName()== user.getUsername() || user.getRole() == UserRoleEnum.ADMIN) {
            reply.update(dto);
        } else {
            throw new CustomException(NO_ACCESS);
        }

        ReplyResponseDto responseDto = new ReplyResponseDto(reply, cnt("Reply", reply.getReplyId()));
        return new MessageDto(StatusEnum.OK, responseDto);
    }

    // 댓글 삭제 기능
    @Transactional
    public MessageDto deleteReply(Long id, Long replyId, User user) {          // 부모클래스인 MessageDto로 리턴타입을 정하고 UserDto도 사용해 다형성 사용
        Memo memo = memoRepository.findById(id).orElseThrow(
                () -> new CustomException(MEMO_NOT_FOUND)
        );

        Reply reply = replyRepository.findByMemo_MemoIdAndReplyId(id, replyId).orElseThrow(
                () -> new CustomException(REPLY_NOT_FOUND)
        );

        if (reply.getReplyName()== user.getUsername() || user.getRole() == UserRoleEnum.ADMIN) {
            replyRepository.deleteByReplyId(replyId);
        } else {
            throw new CustomException(NO_ACCESS);
        }

        return new MessageDto(StatusEnum.OK);

    }

    // 글 좋아요 추가
    public MessageDto hitMemoLike(Long id, User user){
        Memo memo = memoRepository.findById(id).orElseThrow(
                () -> new CustomException(MEMO_NOT_FOUND)
        );

        Optional<MemoLike> likes = memoLikeRepository.findByMemo_MemoIdAndUser_Id(memo.getMemoId(),user.getId());
        if(likes.isPresent()){
            throw new CustomException(DUPLICATE_RESOURCE);
        }

        MemoLike like = new MemoLike(user, memo);
        memoLikeRepository.save(like);

        MemoResponseDtoBuilder mrdBuilder = new MemoResponseDtoBuilder();
        MemoResponseDto responseDto =
                mrdBuilder.id(memo.getMemoId())
                        .title(memo.getTitle())
                        .username(memo.getUsername())
                        .content(memo.getContent())
                        .likeCnt(cnt("Memo",memo.getMemoId()))
                        .createdAt(memo.getCreatedAt())
                        .modifiedAt(memo.getModifiedAt())
                        .addReply(addLikeCntToReplyResponseDto(memo.getReplies()))
                        .getMemos();

        return new MessageDto( StatusEnum.OK, responseDto);
    }

    // 글 좋아요 취소
    public MessageDto cancelMemoLike(Long id, User user){
        Memo memo = memoRepository.findById(id).orElseThrow(
                () -> new CustomException(MEMO_NOT_FOUND)
        );

        Optional<MemoLike> likes = memoLikeRepository.findByMemo_MemoIdAndUser_Id(memo.getMemoId(),user.getId());
        if(likes.isEmpty()){
            throw new CustomException(NOT_FOUND);
        }

        memoLikeRepository.deleteById(likes.get().getId());
        return new MessageDto(StatusEnum.OK);
    }

    // 댓글 좋아요 추가
    public MessageDto hitReplyLike(Long id, Long replyId, User user){
        Memo memo = memoRepository.findById(id).orElseThrow(
                () -> new CustomException(MEMO_NOT_FOUND)
        );

        Reply reply = replyRepository.findByMemo_MemoIdAndReplyId(id, replyId).orElseThrow(
                () -> new CustomException(REPLY_NOT_FOUND)
        );

        Optional<ReplyLike> likes = replyLikeRepository.findByMemo_memoIdAndReply_ReplyIdAndUser_Id(memo.getMemoId(),reply.getReplyId(),user.getId());
        if(likes.isPresent()){
            throw new CustomException(DUPLICATE_RESOURCE);
        }

        ReplyLike like = new ReplyLike(user, memo, reply);
        replyLikeRepository.save(like);

        ReplyResponseDto responseDto = new ReplyResponseDto(reply, cnt("Reply", reply.getReplyId()));
        return new MessageDto(StatusEnum.OK, responseDto);
    }

    // 댓글 좋아요 취소
    public MessageDto cancelReplyLike(Long id, Long replyId, User user){
        Memo memo = memoRepository.findById(id).orElseThrow(
                () -> new CustomException(MEMO_NOT_FOUND)
        );

        Reply reply = replyRepository.findByMemo_MemoIdAndReplyId(id, replyId).orElseThrow(
                () -> new CustomException(REPLY_NOT_FOUND)
        );

        Optional<ReplyLike> likes = replyLikeRepository.findByMemo_memoIdAndReply_ReplyIdAndUser_Id(memo.getMemoId(),reply.getReplyId(),user.getId());
        if(likes.isEmpty()){
            throw new CustomException(NOT_FOUND);
        }

        replyLikeRepository.deleteById(likes.get().getId());
        return new MessageDto(StatusEnum.OK);
    }

    // 좋아요 카운트 조회 기능
    public Long cnt(String entity, Long id){
        Optional<Long> likeCnt;
        switch (entity) {
            case "Memo" -> {
                likeCnt = memoLikeRepository.countByMemo_MemoId(id);
                return likeCnt.get();
            }
            case "Reply" -> {
                likeCnt = replyLikeRepository.countByReply_ReplyId(id);
                return likeCnt.get();
            }
            default -> throw new CustomException(NOT_FOUND);
        }
    }

    // 각 댓글마다 좋아요수 추가
    public List<ReplyResponseDto> addLikeCntToReplyResponseDto(List<Reply> replies){
        List<ReplyResponseDto> exportReplies = new ArrayList<>();
        for(int i=0; i<replies.size(); i++){
            Long test1 = replies.get(i).getReplyId();
            Optional<Long> likeCnt = replyLikeRepository.countByReply_ReplyId(replies.get(i).getReplyId());
            exportReplies.add(new ReplyResponseDto(replies.get(i), likeCnt.get()));
            Long likeCntL = likeCnt.get();
        }

        return exportReplies;
    }
}
