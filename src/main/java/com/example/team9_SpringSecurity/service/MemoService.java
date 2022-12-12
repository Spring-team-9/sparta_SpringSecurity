package com.example.team9_SpringSecurity.service;

import com.example.team9_SpringSecurity.dto.*;
import com.example.team9_SpringSecurity.entity.*;
import com.example.team9_SpringSecurity.repository.LikesRepository;
import com.example.team9_SpringSecurity.repository.MemoRepository;
import com.example.team9_SpringSecurity.repository.ReplyRepository;
import com.example.team9_SpringSecurity.repository.UserRepository;
import com.example.team9_SpringSecurity.util.error.CustomException;
import com.example.team9_SpringSecurity.util.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.team9_SpringSecurity.util.error.ErrorCode.*;

@Service
@RequiredArgsConstructor        // 생성자 자동 주입
public class MemoService {

    private final MemoRepository memoRepository;
    private final UserRepository userRepository;
    private final ReplyRepository replyRepository;
    private final LikesRepository likesRepository;
    private final JwtUtil jwtUtil;


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
    public MessageDto createMemo(MemoRequestDto dto, HttpServletRequest request){
        String token = jwtUtil.resolveToken(request);               // Request에서 Token 가져오기
        Claims claims;                                              // 사용자 정보 Claims을 가져올 변수

        if(token != null){                                          // token없으면 글 생성 불가
            User user = validateUser(token);

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
        } else {
            throw new CustomException(BAD_REQUEST_TOKEN);
        }
    }

    // 글 수정 기능
    @Transactional
    public MessageDto modifyMemo (Long id, MemoRequestDto dto, HttpServletRequest request) {
        Memo memo = memoRepository.findById(id).orElseThrow(
                () -> new CustomException(MEMO_NOT_FOUND)
        );

        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if(token != null) {
            User user = validateUser(token);

            if(accessPermission(memo.getUsername(), user.getUsername(), user.getRole())) {

                memo.update(dto);  // update는 entity에 새로 정의한 함수

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
            throw new CustomException(NO_ACCESS);
        } else {
            throw new CustomException(BAD_REQUEST_TOKEN);
        }
    }


    // 글 삭제 기능
    @Transactional
    public MessageDto deleteMemo (Long id, HttpServletRequest request) {
        Memo memo = memoRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 글이 존재하지 않습니다.")
        );

        String token = jwtUtil.resolveToken(request);

        if(token != null) {
            User user = validateUser(token);

            if(accessPermission(memo.getUsername(), user.getUsername(), user.getRole())) {  // 유저 대조
                memoRepository.deleteById(id);
                return new MessageDto(StatusEnum.OK);
            }
            throw new CustomException(NO_ACCESS);
        }
        throw new CustomException(BAD_REQUEST_TOKEN);
    }

    // 댓글 작성 기능
    public MessageDto createReply(Long id, ReplyRequestDto dto, HttpServletRequest request) {
        Memo memo = memoRepository.findById(id).orElseThrow(
                () -> new CustomException(MEMO_NOT_FOUND)
        );

        String token = jwtUtil.resolveToken(request);

        if(token != null){
            User user = validateUser(token);

            Reply reply = new Reply (dto, user, memo);
            replyRepository.save(reply);
            ReplyResponseDto responseDto = new ReplyResponseDto(reply, cnt("Reply", reply.getReplyId()));
            return new MessageDto(StatusEnum.OK, responseDto);
        }
        throw new CustomException(BAD_REQUEST_TOKEN);
    }

    // 댓글 수정 기능
    @Transactional
    public MessageDto modifyReply(Long id, Long replyId, ReplyRequestDto dto, HttpServletRequest request) {
        Memo memo = memoRepository.findById(id).orElseThrow(
                () -> new CustomException(MEMO_NOT_FOUND)
        );

        Reply reply = replyRepository.findByMemo_MemoIdAndReplyId(id, replyId).orElseThrow(
                () -> new CustomException(REPLY_NOT_FOUND)
        );
        String token = jwtUtil.resolveToken(request);

        if(token != null){
            User user = validateUser(token);

            if(accessPermission(reply.getReplyName(), user.getUsername(), user.getRole())) {
                reply.update(dto);

                ReplyResponseDto responseDto = new ReplyResponseDto(reply, cnt("Reply", reply.getReplyId()));
                return new MessageDto(StatusEnum.OK, responseDto);
            }
            throw new CustomException(NO_ACCESS);
        }
        throw new CustomException(BAD_REQUEST_TOKEN);
    }

    // 댓글 삭제 기능
    @Transactional
    public MessageDto deleteReply(Long id, Long replyId, HttpServletRequest request) {          // 부모클래스인 MessageDto로 리턴타입을 정하고 UserDto도 사용해 다형성 사용
        Memo memo = memoRepository.findById(id).orElseThrow(
                () -> new CustomException(MEMO_NOT_FOUND)
        );

        Reply reply = replyRepository.findByMemo_MemoIdAndReplyId(id, replyId).orElseThrow(
                () -> new CustomException(REPLY_NOT_FOUND)
        );

        String token = jwtUtil.resolveToken(request);

        if(token != null) {
            User user = validateUser(token);

            if (accessPermission(reply.getReplyName(), user.getUsername(), user.getRole())) {
                replyRepository.deleteByReplyId(replyId);
                return new MessageDto(StatusEnum.OK);
            }
            throw new CustomException(NO_ACCESS);
        } else {
            throw new CustomException(BAD_REQUEST_TOKEN);
        }
    }

    // 글 좋아요 추가
    public MessageDto hitMemoLike(Long id, HttpServletRequest request){
        Memo memo = memoRepository.findById(id).orElseThrow(
                () -> new CustomException(MEMO_NOT_FOUND)
        );

        String token = jwtUtil.resolveToken(request);

        if(token != null) {
            User user = validateUser(token);

            Optional<Likes> likes = likesRepository.findByMemo_MemoIdAndReply_ReplyIdIsNullAndUser_Id(memo.getMemoId(),user.getId());
            if(likes.isPresent()){
                throw new CustomException(DUPLICATE_RESOURCE);
            }

            Likes like = new Likes(user, memo);
            likesRepository.save(like);

            MemoResponseDtoBuilder mrdBuilder = new MemoResponseDtoBuilder();
            MemoResponseDto responseDto =
                    mrdBuilder.id(like.getId())
                            .getMemos();

            return new MessageDto( StatusEnum.OK, responseDto);

        } else {
            throw new CustomException(BAD_REQUEST_TOKEN);
        }
    }

    // 글 좋아요 취소
    public MessageDto cancelMemoLike(Long id, HttpServletRequest request){
        Memo memo = memoRepository.findById(id).orElseThrow(
                () -> new CustomException(MEMO_NOT_FOUND)
        );

        String token = jwtUtil.resolveToken(request);

        if(token != null) {
            User user = validateUser(token);

            Optional<Likes> likes = likesRepository.findByMemo_MemoIdAndReply_ReplyIdIsNullAndUser_Id(memo.getMemoId(),user.getId());
            if(likes.isEmpty()){
                throw new CustomException(NOT_FOUND);
            }

            likesRepository.deleteById(likes.get().getId());
            return new MessageDto(StatusEnum.OK);

        } else {
            throw new CustomException(BAD_REQUEST_TOKEN);
        }
    }

    // 댓글 좋아요 추가
    public MessageDto hitReplyLike(Long id, Long replyId, HttpServletRequest request){
        Memo memo = memoRepository.findById(id).orElseThrow(
                () -> new CustomException(MEMO_NOT_FOUND)
        );

        Reply reply = replyRepository.findByMemo_MemoIdAndReplyId(id, replyId).orElseThrow(
                () -> new CustomException(REPLY_NOT_FOUND)
        );

        String token = jwtUtil.resolveToken(request);

        if(token != null) {
            User user = validateUser(token);

            Optional<Likes> likes = likesRepository.findByReply_ReplyIdAndUser_Id(reply.getReplyId(),user.getId());
            if(likes.isPresent()){
                throw new CustomException(DUPLICATE_RESOURCE);
            }

            Likes like = new Likes(user, reply);
            likesRepository.save(like);

            ReplyResponseDto responseDto = new ReplyResponseDto(reply, cnt("Reply", reply.getReplyId()));
            return new MessageDto(StatusEnum.OK, responseDto);

        } else {
            throw new CustomException(BAD_REQUEST_TOKEN);
        }
    }

    // 댓글 좋아요 취소
    public MessageDto cancelReplyLike(Long id, Long replyId, HttpServletRequest request){
        Memo memo = memoRepository.findById(id).orElseThrow(
                () -> new CustomException(MEMO_NOT_FOUND)
        );

        Reply reply = replyRepository.findByMemo_MemoIdAndReplyId(id, replyId).orElseThrow(
                () -> new CustomException(REPLY_NOT_FOUND)
        );

        String token = jwtUtil.resolveToken(request);

        if(token != null) {
            User user = validateUser(token);

            Optional<Likes> likes = likesRepository.findByReply_ReplyIdAndUser_Id(reply.getReplyId(),user.getId());
            if(likes.isEmpty()){
                throw new CustomException(NOT_FOUND);
            }

            likesRepository.deleteById(likes.get().getId());
            return new MessageDto(StatusEnum.OK);

        } else {
            throw new CustomException(BAD_REQUEST_TOKEN);
        }
    }

    // 좋아요 카운트 조회 기능
    public Long cnt(String entity, Long id){
        Optional<Long> likeCnt;
        switch (entity) {
            case "Memo" -> {
                likeCnt = likesRepository.countByMemo_MemoIdAndReply_ReplyIdIsNull(id);
                return likeCnt.get();
            }
            case "Reply" -> {
                likeCnt = likesRepository.countByReply_ReplyId(id);
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
            Optional<Long> likeCnt = likesRepository.countByReply_ReplyId(replies.get(i).getReplyId());
            exportReplies.add(new ReplyResponseDto(replies.get(i), likeCnt.get()));
            Long likeCntL = likeCnt.get();
        }

        return exportReplies;
    }

    // 유저 체크
    public User validateUser(String token){
        Claims claims = null;

        if (jwtUtil.validateToken(token)) {                 // token이 유효한 거면 생성 가능
            claims = jwtUtil.getUserInfoFromToken(token);   // 토큰에서 사용자 정보 가져오기
        } else {
            throw new CustomException(BAD_REQUEST_TOKEN);
        }

        User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                ()-> new CustomException(LOGIN_MATCH_FAIL)
        );

        return user;
    }

    // 작성자 일치 여부 체크 및 ADMIN 허가 적용
    public boolean accessPermission (String nameInEntity, String nameInRequest, UserRoleEnum role ){
        if(nameInEntity.equals(nameInRequest) || role == UserRoleEnum.ADMIN){
            return true;
        } else {
            return false;
        }
    }
}
