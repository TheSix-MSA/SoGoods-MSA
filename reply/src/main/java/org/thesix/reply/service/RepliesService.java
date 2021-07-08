package org.thesix.reply.service;

import org.thesix.reply.dto.ListResponseRepliesDTO;
import org.thesix.reply.dto.RepliesRequestDTO;
import org.thesix.reply.dto.RepliesResponseDTO;
import org.thesix.reply.entity.Replies;

public interface RepliesService {
    ListResponseRepliesDTO getList();
    RepliesResponseDTO saveReply(RepliesRequestDTO dto);

    default RepliesResponseDTO entityToDTO(Replies entity){
        return RepliesResponseDTO.builder()
                .content(entity.getContent())
                .groupId(entity.getGroupId())
                .level(entity.getLevel())
                .modDate(entity.getModDate())
                .parentId(entity.getParentId())
                .regDate(entity.getRegDate())
                .removed(entity.isRemoved())
                .rno(entity.getRno())
                .writer(entity.getWriter())
                .build();
    }

    default Replies DtoToEntity(RepliesRequestDTO dto){
        return Replies.builder()
                .content(dto.getContent())
                .level(dto.getLevel())
                .groupId(dto.getGroupId())
                .parentId(dto.getParentId())
                .keyValue(dto.getKeyValue())
                .writer(dto.getWriter())
                .email(dto.getEmail())
                .build();
    }
}
