package org.thesix.reply.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import org.thesix.reply.dto.ListResponseRepliesDTO;
import org.thesix.reply.dto.RepliesSaveRequestDTO;
import org.thesix.reply.dto.RepliesResponseDTO;
import org.thesix.reply.dto.RepliesUpdateRequestDTO;
import org.thesix.reply.service.RepliesService;

import static org.thesix.reply.common.util.ApiUtil.ApiResult;
import static org.thesix.reply.common.util.ApiUtil.success;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@Log4j2
@RequestMapping("/reply")
public class ReplyController {
    private final RepliesService replyService;

    @GetMapping("/list/{bno}/{page}")
    public ApiResult<ListResponseRepliesDTO> getList(@PathVariable Long bno, @PathVariable Long page){
        /**
         * 해당 개시글의 댓글 목록을 30개까지 페이징 처리해서 보내주는 컨트롤러
         replyService.getList(bno, page)
         */

        return success(replyService.getList(bno, page));
    }

    @PostMapping("/")
    public ApiResult<RepliesResponseDTO> saveReply(@RequestBody RepliesSaveRequestDTO dto){
        /**
         * 댓글 등록 컨트롤러.
         * 대댓글도 이 컨트롤러를 통해서 저장한다.
         *
         */
        return success(replyService.saveReply(dto));
    }

    @PutMapping("/")
    public ApiResult<RepliesResponseDTO> updateReply(@RequestBody RepliesUpdateRequestDTO dto){
        /**
         * 댓글 수정 컨트롤러.
         * 대댓글도 이 컨트롤러를 통해서 수정한다.
         *
         */
        return success(replyService.updateReply(dto));
    }

    @DeleteMapping("/{rno}")
    public ApiResult<Map<String,String>> deleteReply(@PathVariable Long rno){
        /**
         * 댓글 삭제 컨트롤러.
         * 대댓글도 이 컨트롤러를 통해서 삭제 처리한다.
         */
        return success(replyService.deleteReply(rno));
    }

    @GetMapping("/wrote/{page}")
    public ApiResult<ListResponseRepliesDTO> getRepliesMemberWrote(@RequestBody Map<String, String> emailMap, @PathVariable Long page){
        /**
         * 해당 유저가 작성한 댓글의 목록을 가져오는 함수.
         * 유저의 마이 페이지에서 본인이 쓴 댓글 목록을 불러와 그 댓글에 링크 등을 걸어
         * 해당 글로 이동하게 하기위한 컨트롤러러
         * */
        return success(replyService.getListMemberWrote(emailMap, page));
    }
}
