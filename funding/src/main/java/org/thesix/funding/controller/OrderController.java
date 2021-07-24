package org.thesix.funding.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import org.thesix.funding.dto.order.*;
import org.thesix.funding.service.OrderService;

import static org.thesix.funding.util.ApiUtil.ApiResult;
import static org.thesix.funding.util.ApiUtil.success;

@RestController
@RequestMapping("/order")
@CrossOrigin("*")
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
     *
     * @param dto
     * @return
     */
    @GetMapping("/list")
    public ApiResult<OrderPagingResponseDTO> getUsersOrders(@RequestBody OrderPageRequestDTO dto){
        return success(orderService.getUsersOrders(dto));
    }

    /***
     * 결제의 상세 내역을 가져오는 컨트롤러
     *
     * @param ono
     * @return
     */
    @GetMapping("/{ono}")
    public ApiResult<OrderDetailResponseDTO> getOrderDetails(@PathVariable Long ono){
        return success(orderService.getOrder(ono));
    }

    /***
     * 주문 취소 컨트롤러
     * @param ono
     * @return
     */
    @PutMapping("/cancelOrder/{ono}")
    public ApiResult<OrderDetailResponseDTO> cancelOrder(@PathVariable Long ono){
        return success(orderService.cancelOrder(ono));
    }

    /***
     * 주문 배송 컨트롤러
     *
     * @param ono
     * @return
     */
    @PutMapping("/ship/{ono}")
    public ApiResult<OrderDetailResponseDTO> shipOrder(@PathVariable Long ono){
        return success(orderService.shipOrder(ono));
    }

}
