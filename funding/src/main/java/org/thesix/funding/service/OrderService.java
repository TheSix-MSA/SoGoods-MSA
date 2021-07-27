package org.thesix.funding.service;

import org.thesix.funding.dto.funding.FundingDTO;
import org.thesix.funding.dto.order.*;
import org.thesix.funding.entity.Order;
import org.thesix.funding.entity.OrderDetails;
import org.thesix.funding.entity.Product;

import java.util.Map;
import java.util.Set;

public interface OrderService {
    OrderResponseDTO save(OrderSaveRequestDTO dto);
    OrderPagingResponseDTO getUsersOrders(OrderPageRequestDTO dto);
    OrderDetailResponseDTO getOrder(Long ono);
    OrderDetailResponseDTO cancelOrder(Long ono);
    OrderDetailResponseDTO shipOrder(Long ono);

    default Order dtoToOrder(OrderSaveRequestDTO dto){
        return Order.builder().buyer(dto.getBuyer()).tid(dto.getTid())
                .receiverAddress(dto.getReceiverAddress()).receiverDetailedAddress(dto.getReceiverDetailedAddress())
                .receiverName(dto.getReceiverName()).receiverPhone(dto.getReceiverPhone())
                .receiverRequest(dto.getReceiverRequest()).kakaoPayOrderId(dto.getOrderId()).build();
    }

    default OrderDetails mapToOrderDetails(Long i,Map<Long, Long> prods, Order order){
        return OrderDetails.builder().order(order)
                .product(Product.builder().pno(i).build())
                .numProds(prods.get(i)).build();
    }

    default OrderResponseDTO entityToDto(Order dto, Map<Long, Long> prods){
        return OrderResponseDTO.builder().ono(dto.getOno()).buyer(dto.getBuyer()).tid(dto.getTid())
                .receiverAddress(dto.getReceiverAddress()).receiverDetailedAddress(dto.getReceiverDetailedAddress())
                .receiverName(dto.getReceiverName()).receiverPhone(dto.getReceiverPhone())
                .receiverRequest(dto.getReceiverRequest()).regDate(dto.getRegDate().toLocalDate())
                .modDate(dto.getModDate().toLocalDate()).products(prods)
                .shippedDate(dto.getShippedDate()).cancelledDate(dto.getCancelledDate()).build();
    }

    default OrderResponseDTO entityToDto(Order dto){
        return OrderResponseDTO.builder().ono(dto.getOno()).buyer(dto.getBuyer()).tid(dto.getTid())
                .receiverAddress(dto.getReceiverAddress()).receiverDetailedAddress(dto.getReceiverDetailedAddress())
                .receiverName(dto.getReceiverName()).receiverPhone(dto.getReceiverPhone())
                .receiverRequest(dto.getReceiverRequest()).regDate(dto.getRegDate().toLocalDate())
                .modDate(dto.getModDate().toLocalDate()).shippedDate(dto.getShippedDate())
                .cancelledDate(dto.getCancelledDate()).build();
    }

    default ProductInOrderDTO produdctEntityToInOrderDTO(Product product, Long num){
        return ProductInOrderDTO.builder()
                .pno(product.getPno())
                .name(product.getName())
                .price(product.getPrice())
                .des(product.getDes())
                .amount(num).build();
    }

    default OrderDetailResponseDTO orderEntityToDetailedResponseDTO (Order dto, Set<ProductInOrderDTO> prods, FundingDTO funding){
        return OrderDetailResponseDTO.builder().ono(dto.getOno()).buyer(dto.getBuyer()).tid(dto.getTid())
                .receiverAddress(dto.getReceiverAddress()).receiverDetailedAddress(dto.getReceiverDetailedAddress())
                .receiverName(dto.getReceiverName()).receiverPhone(dto.getReceiverPhone())
                .receiverRequest(dto.getReceiverRequest()).regDate(dto.getRegDate().toLocalDate())
                .modDate(dto.getModDate().toLocalDate()).shippedDate(dto.getShippedDate())
                .cancelledDate(dto.getCancelledDate()).prods(prods).fundInfo(funding).build();
    }
}
