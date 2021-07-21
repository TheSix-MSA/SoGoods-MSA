package org.thesix.funding.service;

import org.thesix.funding.common.dto.ListResponseDTO;
import org.thesix.funding.dto.order.OrderPageRequestDTO;
import org.thesix.funding.dto.order.OrderSaveRequestDTO;
import org.thesix.funding.dto.order.OrderResponseDTO;
import org.thesix.funding.entity.Order;
import org.thesix.funding.entity.OrderDetails;
import org.thesix.funding.entity.Product;

import java.util.Map;

public interface OrderService {
    OrderResponseDTO save(OrderSaveRequestDTO dto);
    ListResponseDTO<OrderResponseDTO> getUsersOrders(OrderPageRequestDTO dto);

    default Order dtoToOrder(OrderSaveRequestDTO dto){
        return Order.builder().buyer(dto.getBuyer()).tid(dto.getTid())
                .receiverAddress(dto.getReceiverAddress()).receiverDetailedAddress(dto.getReceiverDetailedAddress())
                .receiverName(dto.getReceiverName()).receiverPhone(dto.getReceiverPhone())
                .receiverRequest(dto.getReceiverRequest()).build();
    }

    default OrderDetails mapToOrderDetails(Long i,Map<Long, Long> prods, Order order){
        return OrderDetails.builder().order(order)
                .product(Product.builder().pno(i).build())
                .numProds(prods.get(i)).build();
    }

    default OrderResponseDTO entityToDto(Order dto, Map<Long, Long> prods){
        return OrderResponseDTO.builder().buyer(dto.getBuyer()).tid(dto.getTid())
                .receiverAddress(dto.getReceiverAddress()).receiverDetailedAddress(dto.getReceiverDetailedAddress())
                .receiverName(dto.getReceiverName()).receiverPhone(dto.getReceiverPhone())
                .receiverRequest(dto.getReceiverRequest()).regDate(dto.getRegDate().toLocalDate())
                .modDate(dto.getModDate().toLocalDate()).products(prods)
                .shippedDate(dto.getShippedDate()).cancelledDate(dto.getCancelledDate()).build();
    }

    default OrderResponseDTO entityToDto(Order dto){
        return OrderResponseDTO.builder().buyer(dto.getBuyer()).tid(dto.getTid())
                .receiverAddress(dto.getReceiverAddress()).receiverDetailedAddress(dto.getReceiverDetailedAddress())
                .receiverName(dto.getReceiverName()).receiverPhone(dto.getReceiverPhone())
                .receiverRequest(dto.getReceiverRequest()).regDate(dto.getRegDate().toLocalDate())
                .modDate(dto.getModDate().toLocalDate()).shippedDate(dto.getShippedDate())
                .cancelledDate(dto.getCancelledDate()).build();
    }
}
