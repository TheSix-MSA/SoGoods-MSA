package org.thesix.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thesix.member.dto.*;
import org.thesix.member.entity.Member;
import org.thesix.member.entity.Novels;
import org.thesix.member.repository.MemberRepository;
import org.thesix.member.repository.NovelRepository;

import java.util.List;
import java.util.stream.Collectors;

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

    @Transactional
    @Override
    public AuthorInfoDTO requestBeAuthor(RequestAuthorDTO dto) {

        Member member = memberRepository.findByEmail(dto.getNovelsDTO().getEmail()).orElseThrow(() -> new NullPointerException("사용자가 존재하지 않습니다"));

        member.changeAuthor(dto.getAuthorInfoDTO());

        member.changeApproval(true);
//
//        Member author = memberRepository.save(member);

        NovelsDTO novels = dto.getNovelsDTO();

        novelRepository.save(Novels.builder()
                .member(Member.builder().email(dto.getNovelsDTO().getEmail()).build())
                .deleted(novels.isDeleted())
                .image(novels.getImage())
                .isbn(novels.getIsbn())
                .publisher(novels.getPublisher())
                .title(novels.getTitle())
                .build());

        return AuthorInfoDTO.builder()
                .identificationUrl(member.getIdentificationUrl())
                .introduce(member.getIntroduce())
                .nickName(member.getNickName())
                .build();
    }

    @Override
    public ResponseNovelList getNovelList(RequestNovelPageDTO dto) {

        Pageable pageable = PageRequest.of(dto.getPage()-1, dto.getSize(), Sort.by("nno").descending());

        Page<Novels> onesNovels = novelRepository.getOnesNovels(pageable, Member.builder().email(dto.getEmail()).build());

        List<NovelsDTO> novelList = onesNovels.stream().map(novels -> NovelsDTO.builder()
                .email(novels.getMember().getEmail())
                .image(novels.getImage())
                .title(novels.getTitle())
                .isbn(novels.getIsbn())
                .publisher(novels.getPublisher())
                .nno(novels.getNno())
                .build()
        ).collect(Collectors.toList());

        PageMaker pageMaker = new PageMaker(pageable,RequestListDTO.builder().page(dto.getPage()).size(dto.getSize()).build(),(int)onesNovels.getTotalElements());

        return ResponseNovelList.builder()
                .novelsDTO(novelList)
                .pageMaker(pageMaker)
                .build();
    }

    @Override
    public NovelsDTO removeNovel(NovelsDTO dto) {

        Novels novels = novelRepository.findById(dto.getNno()).orElseThrow(() -> new NullPointerException("해당 책이 존재하지 않습니다."));

        novels.changeDelete();

        Novels novel = novelRepository.save(novels);

        return entityToNovels(novel);
    }


}
