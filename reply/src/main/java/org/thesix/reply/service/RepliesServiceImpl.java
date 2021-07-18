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
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class RepliesServiceImpl implements RepliesService {
    private final RepliesRepository repository;

    @Override
    public ListResponseRepliesDTO getList(Long bno, Long page) {
        /**
         * 조회하는 게시글 번호(bno), 현재 보려는 페이지(page) 를 받아서
         * 해당 페이지의 댓글을 가져오는 함수.
         */
        if(page < 1L) page = 1L;

        Pageable pageable = PageRequest.of((int) (page-1),30, Sort.by("rno").ascending());

        Page<Replies> res = repository.getList(bno, pageable);

        List<RepliesResponseDTO> listReplyDTO = res.getContent().stream()
                .map(objects -> entityToDTO(objects))
                .collect(Collectors.toList());

        PageMaker pageMaker = new PageMaker(Math.toIntExact(page),30, (int)res.getTotalElements());

        return ListResponseRepliesDTO.builder().repliesDTOList(listReplyDTO).pageMaker(pageMaker).build();
    }

    @Override
    @Transactional
    public RepliesResponseDTO saveReply(RepliesSaveRequestDTO dto) {
        /**
         * 댓글 저장함수. 대댓글이 아닌 일반 댓글의 경우 groupId가 0으로 넘어오는데
         * 이때 0으로 저장 후, 엔티티에 있는 changeGroupId 함수로 저장하며 나온 pk값을
         * groupdId로 바꾼다.
         */

        Replies entity = DtoToEntity(dto);
        Replies entity2 = repository.save(entity);
        if(dto.getGroupId() == null || dto.getGroupId() == 0){
            entity2.changeGroupId(entity.getRno());
        }

        RepliesResponseDTO resDto = entityToDTO(entity);

        return resDto;
    }

    @Override
    @Transactional
    public RepliesResponseDTO updateReply(RepliesUpdateRequestDTO dto) {
        /**
         * 댓글 수정함수.
         * 예외처리까지 완료.(존재하는지 확인 + 수정하려는 유저가 본인인지 확인.)
         */

        Replies replies = repository.findById(dto.getRno())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        if(!dto.getEmail().equals(replies.getEmail()))
            throw new IllegalArgumentException("작성자가 아니라면 변경할 수 없습니다.");

        if(replies.isRemoved()) throw new IllegalArgumentException("이미 지워진 댓글입니다.");

        replies.updateReply(dto.getContent());

        RepliesResponseDTO resDto = entityToDTO(replies);

        return resDto;
    }

    @Override
    @Transactional
    public Map<String, String> deleteReply(Long rno) {
        /**
         * 댓글의 deleted 컬럼을 true로 바꿔 삭제되었다 표기하는 함수.
         * 성공시 'status':'success' 싪패시는 예외 메세지
         *
         * 삭제하려는 유저가 관리자가 맞는지 확인할 방법이 없다.
         * 본인이 맞는지는 확인이 가능(이메일 비교)하나 관리자가 댓글 삭제는 가능한데
         * 요청을 보낸 이메일이 관리자인지 댓글 서비스에선 확인할 방법이 없다.
         */

        Replies replies = repository.findById(rno).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        Map<String, String> res = new HashMap<>();

        replies.deleteReply();
        res.put("status", "댓글이 성공적으로 삭제되었습니다!");

        return res;
    }

    @Override
    public ListResponseRepliesDTO getListMemberWrote(Map<String, String> emailMap, Long page) {
        /***
         * 유저의 이메일을 받아서 그 유저가 작성한 댓글들의 목록을 보내주는 함수.
         */
        if(page < 1L) page = 1L;

        String email = emailMap.get("email");

        if(email.equals("") || email == null) throw new IllegalArgumentException("로그인해야 합니다");

        Pageable pageable = PageRequest.of((int) (page-1), 10, Sort.by("modDate").descending());

        Page<Replies> res = repository.getListUserWrote(email, pageable);

        List<RepliesResponseDTO> listReplyDTO = res.getContent().stream()
                .map(objects -> entityToDTO(objects))
                .collect(Collectors.toList());

        PageMaker pageMaker = new PageMaker(Math.toIntExact(page),10, (int)res.getTotalElements());

        return ListResponseRepliesDTO.builder().repliesDTOList(listReplyDTO).pageMaker(pageMaker).build();
    }
}