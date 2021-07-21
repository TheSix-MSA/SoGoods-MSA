package org.thesix.funding.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import org.thesix.funding.common.dto.ListResponseDTO;
import org.thesix.funding.dto.order.OrderPageRequestDTO;
import org.thesix.funding.dto.order.OrderSaveRequestDTO;
import org.thesix.funding.dto.order.OrderResponseDTO;
import org.thesix.funding.service.OrderService;

import static org.thesix.funding.util.ApiUtil.ApiResult;
import static org.thesix.funding.util.ApiUtil.success;

@RestController
@RequestMapping("/order")
@Log4j2
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    /***
     * order가 들어왔을때 order를 저장하는 함수
     * @param dto
     * @return
     */
    @PostMapping("/")
    public ApiResult<OrderResponseDTO> saveOrder(@RequestBody OrderSaveRequestDTO dto){
        return success(orderService.save(dto));
    }

    /***
     * 리스트 (자기가 산 펀딩 리스트 ==> 페이징 + 검색 + 정렬 조건 필요) 뽑는 컨트롤러
     * @param email
     * @param page
     * @return
     */
    @GetMapping("/list")
    public ApiResult<ListResponseDTO<OrderResponseDTO>> getUsersOrders(@RequestBody OrderPageRequestDTO dto){

        return success(orderService.getUsersOrders(dto));
    }

    /***
     * 필요한 컨트롤러들 (07/20):
     * 1.
     * 2. 상세보기
     * 4. 주문 취소 (결제 관련 로직 필요)
     * 5. 배송 날짜 변경 (어드민 전용)
     */
}
