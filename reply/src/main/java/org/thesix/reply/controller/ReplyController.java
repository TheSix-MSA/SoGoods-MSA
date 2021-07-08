package org.thesix.reply.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thesix.reply.dto.ListResponseRepliesDTO;
import org.thesix.reply.dto.RepliesRequestDTO;
import org.thesix.reply.dto.RepliesResponseDTO;
import org.thesix.reply.service.RepliesService;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/reply")
public class ReplyController {
    private final RepliesService replyService;

    @GetMapping("/list")
    public ResponseEntity<ListResponseRepliesDTO> getList(){
        return ResponseEntity.ok(replyService.getList());
    }

    @PostMapping("/solo")
    public ResponseEntity<RepliesResponseDTO> saveReply(@RequestBody RepliesRequestDTO dto){

        return ResponseEntity.ok(replyService.saveReply(dto));
    }
}
