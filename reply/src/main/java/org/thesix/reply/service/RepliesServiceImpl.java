package org.thesix.reply.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thesix.reply.dto.ListResponseRepliesDTO;
import org.thesix.reply.dto.PageMaker;
import org.thesix.reply.dto.RepliesRequestDTO;
import org.thesix.reply.dto.RepliesResponseDTO;
import org.thesix.reply.entity.Replies;
import org.thesix.reply.repository.RepliesRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class RepliesServiceImpl implements RepliesService {
    private final RepliesRepository repository;


    @Override
    public ListResponseRepliesDTO getList() {
        //Pageable pageable = repliesListRequestDTO.getPageable();

        Pageable pageable = PageRequest.of(0,10);

        Page<Replies> res = repository.getList(pageable);

        List<RepliesResponseDTO> listReplyDTO = res.getContent().stream()
                .map(objects -> entityToDTO(objects))
                .collect(Collectors.toList());

        PageMaker pageMaker = new PageMaker(1,10, (int)res.getTotalElements());

        return ListResponseRepliesDTO.builder().repliesDTOList(listReplyDTO).pageMaker(pageMaker).build();
    }

    @Override
    @Transactional
    public RepliesResponseDTO saveReply(RepliesRequestDTO dto) {
        Replies entity = DtoToEntity(dto);
        entity = repository.save(DtoToEntity(dto));
        if(dto.getGroupId() == null){
            entity.changeGroupId(entity.getRno());
        }

        RepliesResponseDTO resDto = entityToDTO(entity);

        return resDto;
    }
}
