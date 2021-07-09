package org.thesix.member.service;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.thesix.member.dto.MemberDTO;
import org.thesix.member.dto.RequestListDTO;
import org.thesix.member.entity.Member;
import org.thesix.member.repository.MemberRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    @Override
    public String regist(MemberDTO dto) {

        Member member = memberRepository.save(memberDTOToEntity(dto));

        return member.getEmail();
    }

    @Override
    public String readUser(String email) {

        Optional<Member> result = memberRepository.findById(email);
        Member member = result.get();

        if(result.isPresent()){

            MemberDTO dto = entityToMeberDTO(member);

            Gson gson = new Gson();
            String dtoStr = gson.toJson(dto);

            return dtoStr;
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
    public List<MemberDTO> readList(RequestListDTO dto) {
//        Pageable pageable = PageRequest.of(1, 10);
//        Page<Member> memberList = memberRepository.getMemberList("1", "2", pageable);
//        List<MemberDTO> collect = memberList.getContent().stream()
//                .map(member -> entityToMeberDTO(member)).collect(Collectors.toList());
//
//        return collect;
        return null;
    }
}
