package org.thesix.funding.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import org.thesix.funding.common.dto.ListResponseDTO;
import org.thesix.funding.dto.*;
import org.thesix.funding.service.FundingService;
import static org.thesix.funding.util.ApiUtil.ApiResult;
import static org.thesix.funding.util.ApiUtil.success;
import java.util.List;

@RestController
@RequestMapping("/funding")
@Log4j2
@RequiredArgsConstructor
public class FundingController {

    private final FundingService fundingService;

    /**
     * 글 정보를 입력받아 저장하는 메서드
     * @param registerDTO
     * @return ApiResult<FundingDTO>
     */
    @PostMapping("/")
    public ApiResult<FundingDTO> register(@RequestBody FundingRegisterDTO registerDTO){

        return success(fundingService.register(registerDTO));
    }

    /**
     * 전체 글 리스트와 상품 수, 찜 수를 가져올 메서드
     * @param fundingRequestDTO
     * @return ApiResult<ListResponseDTO<ListFundingDTO>>
     */
    @GetMapping("/list")
    public ApiResult<ListResponseDTO<ListFundingDTO>> getList(FundingRequestDTO fundingRequestDTO){

        return success(fundingService.getSearchList(fundingRequestDTO));
    }

    /**
     * 글 번호를 받아 세부화면에 필요한 정보를 불러올 메서드
     * @param fno
     * @return ApiResult<FundingResponseDTO>
     */
    @GetMapping("/{fno}")
    public ApiResult<FundingResponseDTO> getOneFundingData(@PathVariable Long fno){

        return success(fundingService.getDetailFundingData(fno));
    }

    /**
     * 해당 유저가 쓴 게시글 리스트를 불러올 메서드
     * @param email
     * @return List<FundingDTO>
     */
    @GetMapping("/user/list")
    public ApiResult<List<FundingDTO>> getFundingList(@RequestParam String email){

        return success(fundingService.getFundingList(email));
    }

    /**
     * 펀딩 글을 수정하는 메서드 (제목, 내용, 만기일, 모금 금액만 수정가능)
     * @param fno
     * @param fundingModDTO
     * @return ApiResult<FundingResponseDTO>
     */
    @PutMapping("/{fno}")
    public ApiResult<FundingResponseDTO> Modify(@PathVariable Long fno, @RequestBody FundingModDTO fundingModDTO){

        return success(fundingService.modify(fno, fundingModDTO));
    }

    /**
     * 펀딩 글을 삭제하는 메서드 -> removed 처리
     * @param fno
     * @return ApiResult<FundingResponseDTO>
     */
    @DeleteMapping("/{fno}")
    public ApiResult<FundingResponseDTO> changeRemoved(@PathVariable Long fno){

        return success(fundingService.remove(fno));
    }

    /**
     * 찜하기 기능
     * @param favoriteDTO
     * @return ApiResult<Long>  => 추가된 게시판의 총 좋아요 수 반환
     */
    @PostMapping("/fav")
    public ApiResult<Long> changeFavorite(@RequestBody FavoriteDTO favoriteDTO){

        return success(fundingService.insertFavorite(favoriteDTO));
    }

    /**
     * 해당 유저가 찜한 펀딩 게시글 리스트
     * @param email
     * @return ApiResult<List<FundingDTO>>
     */
    @GetMapping("/fav/list")
    public ApiResult<List<FundingDTO>> getFavoriteFunding(@RequestParam String email){

      return success(fundingService.getFavoriteFunding(email));
    }






}
