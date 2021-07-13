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
        //Pageable pageable = repliesListRequestDTO.getPageable();

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
        Optional<Replies> optReply = repository.findById(dto.getRno());
        /**
         * Not exactly sure if we need to consider if the attempting user is actually the owner of the reply he/she is trying to modify
         */
        RepliesResponseDTO resDto = null;
        if(optReply.isPresent()){
            Replies replies = optReply.get();
            replies.updateReply(dto.getContent());
            resDto = entityToDTO(replies);
        } else {
            /**
             * Needs to have some sort of exception handles here
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
