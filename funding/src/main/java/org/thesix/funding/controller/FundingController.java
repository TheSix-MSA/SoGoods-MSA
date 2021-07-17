package org.thesix.funding.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thesix.funding.common.dto.ListResponseDTO;
import org.thesix.funding.dto.*;
import org.thesix.funding.service.FundingService;

@RestController
@RequestMapping("/funding")
@Log4j2
@RequiredArgsConstructor
public class FundingController {

    private final FundingService fundingService;

    /**
     * 글 정보를 입력받아 저장하는 메서드
     * @param registerDTO
     * @return ResponseEntity<FundingDTO>
     */
    @PostMapping("/")
    public ResponseEntity<FundingDTO> register(@RequestBody FundingRegisterDTO registerDTO){

        return ResponseEntity.ok(fundingService.register(registerDTO));
    }

    /**
     * 전체 글 리스트와 상품 수, 찜 수를 가져올 메서드
     * @param fundingRequestDTO
     * @return ResponseEntity<ListResponseDTO<ListFundingDTO>>
     */
    @GetMapping("/list")
    public ResponseEntity<ListResponseDTO<ListFundingDTO>> getList(FundingRequestDTO fundingRequestDTO){

        return ResponseEntity.ok(fundingService.getSearchList(fundingRequestDTO));
    }

    /**
     * 지정한 글 번호를 받아 세부화면에 필요한 정보를 불러올 메서드
     * @param fno
     * @return ResponseEntity<FundingResponseDTO>
     */
    @GetMapping("/{fno}")
    public ResponseEntity<FundingResponseDTO> getFundingData(@PathVariable Long fno){

        return ResponseEntity.ok(fundingService.getData(fno));
    }

    /**
     * 펀딩 글을 수정하는 메서드 (제목, 내용, 만기일, 모금 금액만 수정가능)
     * @param fno
     * @param registerDTO
     * @return ResponseEntity<FundingResponseDTO>
     */
    @PutMapping("/{fno}")
    public ResponseEntity<FundingResponseDTO> Modify(@PathVariable Long fno, @RequestBody FundingRegisterDTO registerDTO){

        return ResponseEntity.ok().body(fundingService.modify(fno, registerDTO));
    }


    /**
     * 펀딩 글을 삭제하는 메서드 -> removed 처리
     * @param fno
     * @return ResponseEntity<FundingResponseDTO>
     */
    @DeleteMapping("/{fno}")
    public ResponseEntity<FundingResponseDTO> changeRemoved(@PathVariable Long fno){

        return ResponseEntity.ok(fundingService.remove(fno));
    }

    /**
     * 찜하기 기능
     * @param favoriteDTO
     * @return ResponseEntity<Long>  => 추가된 게시판의 총 좋아요 수 반환
     */
    @PostMapping("/fav")
    public ResponseEntity<Long> changeFavorite(@RequestBody FavoriteDTO favoriteDTO){

        return ResponseEntity.ok(fundingService.insertFavorite(favoriteDTO));
    }


    /**
     * 해당 유저가 찜한 펀딩 게시글 리스트
     * @param email
     * @return
     */
    @GetMapping
    public ResponseEntity<FundingDTO[]> getFavoriteFunding(String email){

        return ResponseEntity.ok(null);
    }




}
