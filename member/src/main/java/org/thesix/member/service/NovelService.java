package org.thesix.member.service;

import org.thesix.member.dto.*;
import org.thesix.member.entity.Member;
import org.thesix.member.entity.Novels;

public interface NovelService {
    NovelsDTO registerNovel(NovelsDTO dto);

    AuthorInfoDTO requestBeAuthor(RequestAuthorDTO dto);

    ResponseNovelList getNovelList(RequestNovelPageDTO dto);

    default Novels dtoToEntity(NovelsDTO dto){
        return Novels.builder()
                .nno(dto.getNno())
                .isbn(dto.getIsbn())
                .title(dto.getTitle())
                .publisher(dto.getPublisher())
                .image(dto.getImage())
                .member(Member.builder().email(dto.getEmail()).build())
                .deleted(dto.isDeleted())
                .build();
    }

    default NovelsDTO entityToNovels(Novels entity){
        return NovelsDTO.builder()
                .nno(entity.getNno())
                .isbn(entity.getIsbn())
                .title(entity.getTitle())
                .publisher(entity.getPublisher())
                .image(entity.getImage())
                .email(entity.getMember().getEmail())
                .deleted(entity.isDeleted())
                .build();
    }
}
