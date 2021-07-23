package org.thesix.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import org.thesix.member.dto.*;
import org.thesix.member.service.NovelService;
import static org.thesix.member.util.ApiUtil.ApiResult;
import static org.thesix.member.util.ApiUtil.success;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class NovelController {

    private final NovelService novelService;

    /**
     * 작품등록
     *
     * @param dto
     * @return
     */
    @PostMapping("/novels")
    public ApiResult<NovelsDTO> registerNovels(@RequestBody NovelsDTO dto){
        log.info(dto);
        return success(novelService.registerNovel(dto));
    }


    /**
     * 최초 작가 등록
     *
     * {
     *     "novelsDTO":{
     *         "isbn":"9788970127248",
     *         "image":"https://image.aladin.co.kr/product/61/50/coversum/8970127240_2.jpg",
     *         "publisher":"문학사상사",
     *         "title":"총 균 쇠 (반양장) - 무기.병균.금속은 인류의 운명을 어떻게 바꿨는가, 개정증보판",
     *         "email":"diqksk@naver.com"
     *     },
     *     "authorInfoDTO":{
     *         "identificationUrl":"https://cdn.womancs.co.kr/news/photo/201905/51528_45254_5156.jpg",
     *         "nickName":"보노리아",
     *         "introduce":"내자기소개입니다"
     *     }
     * }
     *
     *
     * @param dto
     * @return
     */
    @PutMapping("/novels")
    public ApiResult<AuthorInfoDTO> requestBeAuthor(@RequestBody RequestAuthorDTO dto){
        log.info(dto);
        return success(novelService.requestBeAuthor(dto));
    }

    @GetMapping("/novels")
    public ApiResult<ResponseNovelList> getNovels(RequestNovelPageDTO dto){

        return success(novelService.getNovelList(dto));
    }


}
