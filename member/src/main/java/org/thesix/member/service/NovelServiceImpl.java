package org.thesix.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.thesix.member.dto.AuthorInfoDTO;
import org.thesix.member.dto.NovelsDTO;
import org.thesix.member.dto.RequestAuthorDTO;
import org.thesix.member.entity.Member;
import org.thesix.member.entity.Novels;
import org.thesix.member.repository.MemberRepository;
import org.thesix.member.repository.NovelRepository;

@Service
@Log4j2
@RequiredArgsConstructor
public class NovelServiceImpl implements NovelService{

    private final NovelRepository novelRepository;
    private final MemberRepository memberRepository;

    @Override
    public NovelsDTO registerNovel(NovelsDTO dto) {
        Novels novels = novelRepository.save(dtoToEntity(dto));
        return entityToNovels(novels);
    }

    @Override
    public AuthorInfoDTO requestBeAuthor(RequestAuthorDTO dto) {

        Member member = memberRepository.findByEmail(dto.getNovelsDTO().getEmail()).orElseThrow(() -> new NullPointerException("사용자가 존재하지 않습니다"));

        member.changeAuthor(dto.getAuthorInfoDTO());

        member.changeApproval(true);

        Member author = memberRepository.save(member);

        NovelsDTO novels = dto.getNovelsDTO();

        novelRepository.save(Novels.builder()
                .member(Member.builder().email(dto.getNovelsDTO().getEmail()).build())
                .deleted(novels.isDeleted())
                .isbn(novels.getIsbn())
                .publisher(novels.getPublisher())
                .title(novels.getTitle())
                .build());

        return AuthorInfoDTO.builder()
                .identificationUrl(author.getIdentificationUrl())
                .introduce(author.getIntroduce())
                .nickName(author.getNickName())
                .build();
    }


}
