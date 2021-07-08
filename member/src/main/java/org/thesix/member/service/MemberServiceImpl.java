package org.thesix.member.service;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.thesix.member.dto.MemberDTO;
import org.thesix.member.entity.Member;
import org.thesix.member.repository.MemberRepository;

import java.util.Optional;

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
}
