package org.thesix.reply.service;

import org.thesix.reply.dto.ListResponseRepliesDTO;
import org.thesix.reply.dto.RepliesSaveRequestDTO;
import org.thesix.reply.dto.RepliesResponseDTO;
import org.thesix.reply.dto.RepliesUpdateRequestDTO;
import org.thesix.reply.entity.Replies;

import java.util.Map;

public interface RepliesService {
    ListResponseRepliesDTO getList(Long bno, int page);
    RepliesResponseDTO saveReply(RepliesSaveRequestDTO dto);
    RepliesResponseDTO updateReply(RepliesUpdateRequestDTO dto);
    Map<String, String> deleteReply(Long rno);

    default RepliesResponseDTO entityToDTO(Replies entity){
        return RepliesResponseDTO.builder()
                .content(entity.getContent())
                .email(entity.getEmail())
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

    default Replies DtoToEntity(RepliesSaveRequestDTO dto){
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
