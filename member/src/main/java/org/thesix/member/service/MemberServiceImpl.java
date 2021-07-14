package org.thesix.member.service;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thesix.member.dto.MemberDTO;
import org.thesix.member.dto.PageMaker;
import org.thesix.member.dto.RequestListDTO;
import org.thesix.member.dto.ResponseListDTO;
import org.thesix.member.entity.Member;
import org.thesix.member.repository.MemberRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    private final PasswordEncoder encoder;

    /**
     * 회원등록
     * @param dto
     * @return
     */
    @Override
    public MemberDTO register(MemberDTO dto) {
        dto.setPassword(encoder.encode(dto.getPassword()));
        Member member = memberRepository.save(memberDTOToEntity(dto));

        return entityToMeberDTO(member);
    }

    @Override
    public MemberDTO readUser(String email) {

        Optional<Member> result = memberRepository.findById(email);
        Member member = result.get();

        if(result.isPresent()){

            MemberDTO dto = entityToMeberDTO(member);


            return dto;
        };
        return null;
    }

    @Override
    public String delete(String email) {
        Member member = Member.builder().email(email).build();
        memberRepository.delete(member);
        return email;
    }

    @Override
    public MemberDTO modify(MemberDTO dto) {

        Optional<Member> result = memberRepository.findById(dto.getEmail());

        if(result.isPresent()){
            Member member = result.get();


            member.changeMemberInfo(memberDTOToEntity(dto));

            Member inputResult = memberRepository.save(member);

            return entityToMeberDTO(inputResult);
        }

        return null;
    }

    @Override
    public ResponseListDTO readList(RequestListDTO dto) {

        Pageable pageable = PageRequest.of(dto.getPage(), dto.getSize());

        Page<Object[]> memberList = memberRepository.getMemberList(dto.getType(), dto.getKeyword(), pageable);

        List<Object[]> memberListResult = memberList.toList();

        PageMaker pageMaker = new PageMaker(pageable, dto, (int) memberList.getTotalElements());


        return   ResponseListDTO.builder()
                .requestListDTO(dto)
                .memberList(memberListResult)
                .pageMaker(pageMaker)
                .build();

    }
}
