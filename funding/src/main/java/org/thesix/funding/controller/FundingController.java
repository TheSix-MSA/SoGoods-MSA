package org.thesix.funding.controller;

import jdk.jshell.Snippet;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thesix.funding.common.dto.ListResponseDTO;
import org.thesix.funding.dto.FundingDTO;
import org.thesix.funding.dto.FundingRegisterDTO;
import org.thesix.funding.dto.FundingRequestDTO;
import org.thesix.funding.dto.ListFundingDTO;
import org.thesix.funding.service.FundingService;

@RestController
@RequestMapping("/funding")
@Log4j2
@RequiredArgsConstructor
public class FundingController {

    private final FundingService fundingService;

    /**
     * 글 정보를 입력받아 저장하는 메서드
     * @param fundingDTO
     * @return ResponseEntity<FundingDTO>
     */
    @PostMapping("/register")
    public ResponseEntity<FundingDTO> register(@RequestBody FundingRegisterDTO registerDTO){

        log.info(registerDTO);

        return ResponseEntity.ok(fundingService.register(registerDTO));
    }

    /**
     * 전체 글 리스트와 상품 수, 찜 수를 가져올 메서드
     * @param fundingRequestDTO
     * @return ResponseEntity<ListResponseDTO<ListFundingDTO>>
     */
    @GetMapping("/list")
    public ResponseEntity<ListResponseDTO<ListFundingDTO>> getList(FundingRequestDTO fundingRequestDTO){

        log.info(fundingRequestDTO);

        return ResponseEntity.ok(fundingService.getSearchList(fundingRequestDTO));
    }

    /**
     * 지정한 글 번호를 받아 세부화면에 필요한 정보를 불러올 메서드 -- 미완
     * @param fno
     * @return
     */
    @GetMapping("/read/{fno}")
    public ResponseEntity<ListFundingDTO> getFundingData(@PathVariable Long fno){

        return ResponseEntity.ok(fundingService.getData(fno));
    }

    @GetMapping("/update/{fno}")
    public ResponseEntity<FundingDTO> Modify(@PathVariable Long fno){

        return ResponseEntity.ok().body(null);
    }


}
