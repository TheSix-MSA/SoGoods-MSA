package org.thesix.funding.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import org.thesix.funding.common.dto.ListRequestDTO;
import org.thesix.funding.common.dto.ListResponseDTO;
import org.thesix.funding.dto.FavoriteRequestDTO;
import org.thesix.funding.dto.FavoriteResponseDTO;
import org.thesix.funding.dto.FundingRegResponseDTO;
import org.thesix.funding.dto.funding.*;
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
     * @return ApiResult<FundingRegResponseDTO>
     */
    @PostMapping("/")
    public ApiResult<FundingRegResponseDTO> register(@RequestBody FundingRegisterDTO registerDTO){

        return success(fundingService.register(registerDTO));
    }

    /**
     * 펀딩의 글 리스트와 상품 수, 찜 수를 가져올 메서드
     * param state : open -> 진행중인 펀딩, close => 종료된 펀딩
     * @param fundingRequestDTO
     * @return ApiResult<ListResponseDTO<ListFundingDTO>>
     */
    @GetMapping("/list")
    public ApiResult<ListResponseDTO<ListFundingDTO>> getOpenList(FundingRequestDTO fundingRequestDTO){

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
    @GetMapping("/user/list/{email}")
    public ApiResult<List<FundingDTO>> getFundingList(@PathVariable String email){

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
    public ApiResult<FundingDeletionResponseDTO> changeRemoved(@PathVariable Long fno){

        return success(fundingService.remove(fno));
    }


    /**
     * 해당 게시글의 찜 리스트 가져오기
     * @param fno
     * @return ApiResult<FavoriteResponseDTO>
     */
    @GetMapping("/fav/{fno}")
     public ApiResult<FavoriteResponseDTO> getFavoriteList(@PathVariable Long fno){

        return success(fundingService.getFavoriteList(fno));
     }

    /**
     * 찜하기 기능
     * @param favoriteRequestDTO
     * @return FavoriteResponseDTO
     */
    @PostMapping("/fav")
    public ApiResult<FavoriteResponseDTO> changeFavorite(@RequestBody FavoriteRequestDTO favoriteRequestDTO){

        return success(fundingService.insertFavorite(favoriteRequestDTO));
    }

    /**
     * 해당 유저가 찜한 펀딩 게시글 리스트
     * @param email
     * @return ApiResult<List<FundingDTO>>
     */
    @GetMapping("/fav/list/{email}")
    public ApiResult<List<FundingDTO>> getFavoriteFunding(@PathVariable String email){

      return success(fundingService.getFavoriteFunding(email));
    }


    /**
     * 게시글 승인처리를 하는 메서드
     * @param fno
     * @return FundingDTO
     */
    @PutMapping("/req/{fno}")
    public ApiResult<FundingDTO> changeAuthorized(@PathVariable Long fno){

        return success(fundingService.updateAuthorized(fno));
    }

    /**
     * 승인되지 않은 게시물 리스트를 가져오는 메서드
     * + 페이징처리
     * @return List<FundingDTO>
     */
    @GetMapping("/false/list")
    public ApiResult<ListResponseDTO<FundingDTO>> getAuthorizedFalse(ListRequestDTO dto){

        return success(fundingService.getNotAuthorizedFunding(dto));
    }





}
