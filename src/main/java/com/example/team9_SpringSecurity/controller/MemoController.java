package com.example.team9_SpringSecurity.controller;

import com.example.team9_SpringSecurity.dto.MemoRequestDto;
import com.example.team9_SpringSecurity.dto.MessageDto;
import com.example.team9_SpringSecurity.dto.ReplyRequestDto;
import com.example.team9_SpringSecurity.service.MemoService;
import com.example.team9_SpringSecurity.util.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController             // 컨트롤러 선언
@RequiredArgsConstructor    // final 변수, Notnull 표시가 된 변수처럼 필수적인 정보를 세팅하는 생성자를 만든다.
public class MemoController {

    private final MemoService memoService;

    // ----------------- 글 기능
    // 전체글 조회
    @GetMapping("/api/memos") // GET방식
    public ResponseEntity<MessageDto> getMemos (){
        MessageDto messageDto = memoService.getMemos();
        return new ResponseEntity<>(messageDto, HttpStatus.OK);
    }

    // 선택글 조회
    @GetMapping("/api/memos/{id}") // GET방식
    public ResponseEntity<MessageDto> getMemo (@PathVariable Long id){
        MessageDto messageDto = memoService.getMemos(id);
        return new ResponseEntity<>(messageDto, HttpStatus.OK);
    }

    // 글 작성
    @PostMapping("/api/memos")  //POST방식
    public ResponseEntity<MessageDto> createMemo (
            @RequestBody MemoRequestDto dto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        MessageDto messageDto = memoService.createMemo(dto, userDetails.getUser());
        return new ResponseEntity<>(messageDto, HttpStatus.OK);
    }

    // 글 수정
    @PutMapping("/api/memos/{id}") // PUT방식
    public ResponseEntity<MessageDto> modifyMemo(
            @PathVariable Long id,
            @RequestBody MemoRequestDto dto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        MessageDto messageDto = memoService.modifyMemo(id, dto, userDetails.getUser());
        return new ResponseEntity<>(messageDto, HttpStatus.OK);
    }

    // 글 삭제
    @DeleteMapping("/api/memos/{id}")  // DELETE방식
    public ResponseEntity<MessageDto> deleteMemo(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        MessageDto messageDto = memoService.deleteMemo(id, userDetails.getUser());
        return new ResponseEntity<>(messageDto, HttpStatus.OK);
    }


    // ---------------댓글 기능
    // 댓글 작성
    @PostMapping("/api/memos/{id}")
    public ResponseEntity<MessageDto> createReply(
            @PathVariable Long id,
            @RequestBody ReplyRequestDto dto,
            @AuthenticationPrincipal UserDetailsImpl userDetails){

        MessageDto messageDto = memoService.createReply(id, dto, userDetails.getUser());
        return new ResponseEntity<>(messageDto, HttpStatus.OK);
    }

    // 댓글 수정
    @PutMapping("/api/memos/{id}/{replyId}")
    public ResponseEntity<MessageDto> modifyReply(
            @PathVariable Long id,
            @PathVariable Long replyId,
            @RequestBody ReplyRequestDto dto,
            @AuthenticationPrincipal UserDetailsImpl userDetails){

        MessageDto messageDto = memoService.modifyReply(id, replyId, dto, userDetails.getUser());
        return new ResponseEntity<>(messageDto, HttpStatus.OK);
    }

    // 댓글 삭제
    @DeleteMapping("/api/memos/{id}/{replyId}")
    public ResponseEntity<MessageDto> DeleteReply(
            @PathVariable Long id,
            @PathVariable Long replyId,
            @AuthenticationPrincipal UserDetailsImpl userDetails){

        MessageDto messageDto = memoService.deleteReply(id, replyId, userDetails.getUser());
        return new ResponseEntity<>(messageDto, HttpStatus.OK);
    }

    // 글 좋아요 클릭
    @PostMapping("/api/memos/like/{id}")
    public ResponseEntity<MessageDto> hitMemoLike(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails){

        MessageDto messageDto = memoService.hitMemoLike(id, userDetails.getUser());
        return new ResponseEntity<>(messageDto, HttpStatus.OK);
    }

    // 글 좋아요 클릭
    @DeleteMapping("/api/memos/like/{id}")
    public ResponseEntity<MessageDto> cancelTheLike(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails){

        MessageDto messageDto = memoService.cancelMemoLike(id, userDetails.getUser());
        return new ResponseEntity<>(messageDto, HttpStatus.OK);
    }

    // 댓글 좋아요 클릭
    @PostMapping("/api/memos/like/{id}/{replyId}")
    public ResponseEntity<MessageDto> hitMemoLike(
            @PathVariable Long id,
            @PathVariable Long replyId,
            @AuthenticationPrincipal UserDetailsImpl userDetails){

        MessageDto messageDto = memoService.hitReplyLike(id, replyId, userDetails.getUser());
        return new ResponseEntity<>(messageDto, HttpStatus.OK);
    }

    // 댓글 좋아요 클릭
    @DeleteMapping("/api/memos/like/{id}/{replyId}")
    public ResponseEntity<MessageDto> cancelTheLike(
            @PathVariable Long id,
            @PathVariable Long replyId,
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        MessageDto messageDto = memoService.cancelReplyLike(id, replyId, userDetails.getUser());
        return new ResponseEntity<>(messageDto, HttpStatus.OK);
    }
}