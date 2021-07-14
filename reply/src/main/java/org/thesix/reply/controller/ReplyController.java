package org.thesix.reply.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thesix.reply.dto.ListResponseRepliesDTO;
import org.thesix.reply.dto.RepliesSaveRequestDTO;
import org.thesix.reply.dto.RepliesResponseDTO;
import org.thesix.reply.dto.RepliesUpdateRequestDTO;
import org.thesix.reply.service.RepliesService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
//@CrossOrigin(origins = {"*"})
@RequestMapping("/reply")
public class ReplyController {
    private final RepliesService replyService;

    @GetMapping("/list/{bno}/{page}")
    public ResponseEntity<ListResponseRepliesDTO> getList(@PathVariable Long bno, @PathVariable int page){
        /**
         * 해당 개시글의 댓글 목록을 30개까지 페이징 처리해서 보내주는 컨트롤러
         */
        return ResponseEntity.ok(replyService.getList(bno, page));
    }

    @PostMapping("/save")
    public ResponseEntity<RepliesResponseDTO> saveReply(@RequestBody RepliesSaveRequestDTO dto){
        /**
         * 댓글 등록 컨트롤러.
         * 대댓글도 이 컨트롤러를 통해서 저장한다.
         *
         */
        return ResponseEntity.ok(replyService.saveReply(dto));
    }

    @PutMapping("/update")
    public ResponseEntity<RepliesResponseDTO> updateReply(@RequestBody RepliesUpdateRequestDTO dto){
        /**
         * 댓글 수정 컨트롤러.
         * 대댓글도 이 컨트롤러를 통해서 수정한다.
         *
         */
        return ResponseEntity.ok(replyService.updateReply(dto));
    }

    @DeleteMapping("/delete/{rno}")
    public ResponseEntity<Map<String,String>> updateReply(@PathVariable Long rno){
        /**
         * 댓글 삭제 컨트롤러.
         * 대댓글도 이 컨트롤러를 통해서 삭제 처리한다.
         *
         */
        return ResponseEntity.ok(replyService.deleteReply(rno));
    }
}
