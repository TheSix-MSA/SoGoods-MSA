package org.thesix.funding.controller;

import jdk.jshell.Snippet;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thesix.funding.common.dto.ListRequestDTO;
import org.thesix.funding.common.dto.ListResponseDTO;
import org.thesix.funding.dto.FundingRequestDTO;
import org.thesix.funding.dto.ListFundingDTO;
import org.thesix.funding.service.FundingService;

@RestController
@RequestMapping("/fundings")
@Log4j2
@RequiredArgsConstructor
public class FundingController {

    private final FundingService fundingService;

//    @GetMapping("/list")
//    public ResponseEntity<String> getList(FundingRequestDTO fundingRequestDTO){
//
//        log.info(fundingRequestDTO);
//
//        //return ResponseEntity.ok(fundingService.getSearchList(fundingRequestDTO));
//        return ResponseEntity.ok().body("200");
//    }

    @GetMapping("/list")
    public ResponseEntity<ListResponseDTO<ListFundingDTO>> getList(FundingRequestDTO fundingRequestDTO){

        log.info(fundingRequestDTO);

        return ResponseEntity.ok(fundingService.getSearchList(fundingRequestDTO));
    }
}
