package org.thesix.reply.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thesix.reply.dto.*;
import org.thesix.reply.entity.Replies;
import org.thesix.reply.repository.RepliesRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class RepliesServiceImpl implements RepliesService {
    private final RepliesRepository repository;


    @Override
    public ListResponseRepliesDTO getList(Long bno, int page) {
        /**
         * 조회하는 게시글 번호(bno), 현재 보려는 페이지(page) 를 받아서
         * 해당 페이지의 댓글을 가져오는 함수.
         */
        Pageable pageable = PageRequest.of(page-1,30, Sort.by("rno").ascending());

        Page<Replies> res = repository.getList(bno, pageable);

        List<RepliesResponseDTO> listReplyDTO = res.getContent().stream()
                .map(objects -> entityToDTO(objects))
                .collect(Collectors.toList());

        PageMaker pageMaker = new PageMaker(page,30, (int)res.getTotalElements());

        return ListResponseRepliesDTO.builder().repliesDTOList(listReplyDTO).pageMaker(pageMaker).build();
    }

    @Override
    @Transactional
    public RepliesResponseDTO saveReply(RepliesSaveRequestDTO dto) {
        /**
         * 댓글 저장함수. 대댓글이 아닌 일반 댓글의 경우 groupId가 0으로 넘어오는데
         * 이때 0으로 저장 후, 엔티티에 있는 changeGroupId 함수로 저장하며 나온 pk값을
         * groupdId로 바꾼다.
         */
        Replies entity = null;
        entity = repository.save(DtoToEntity(dto));
        if(dto.getGroupId() == null || dto.getGroupId() == 0){
            entity.changeGroupId(entity.getRno());
        }

        RepliesResponseDTO resDto = entityToDTO(entity);

        return resDto;
    }

    @Override
    @Transactional
    public RepliesResponseDTO updateReply(RepliesUpdateRequestDTO dto) {
        /**
         * 댓글 수정함수.
         */

        Optional<Replies> optReply = repository.findById(dto.getRno());
        RepliesResponseDTO resDto = null;
        if(optReply.isPresent()){
            Replies replies = optReply.get();
            if(replies.isRemoved()){
                /**
                 * 댓글이 지워져 있다면?
                 * 프론트에서 안보이게 처리 필요
                 *
                 * 예외처리에 대해 공부후 추가 필요
                 */
            }
            replies.updateReply(dto.getContent());
            resDto = entityToDTO(replies);
        } else {
            /**
             * 없는 댓글에 대한 예외처리.
             */
        }

        return resDto;
    }

    @Override
    @Transactional
    public Map<String, String> deleteReply(Long rno) {
        /**
         * 댓글의 deleted 컬럼을 true로 바꿔 삭제되었다 표기하는 함수.
         * 성공시 'status':'success' 싪패시는 예외 메세지
         */

        Optional<Replies> optReply = repository.findById(rno);
        /**
         * Not exactly sure if we need to consider if the attempting user is actually the owner of the reply he/she is trying to modify
         */

        Map<String, String> res = new HashMap<>();


        if(optReply.isPresent()){
            Replies replies = optReply.get();
            replies.deleteReply();
            res.put("status", "댓글이 성공적으로 삭제되었습니다!");
        } else {
            /**
             * Needs to have some sort of exception handles here
             */

            res.put("status", "실패!!");
        }
        return res;
    }
}
