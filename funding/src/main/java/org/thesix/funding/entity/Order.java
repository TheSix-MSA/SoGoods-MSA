package org.thesix.funding.entity;

import lombok.*;
import org.thesix.funding.common.entity.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "tbl_order", indexes = @Index(name = "idx_order", columnList = "ono"))
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ono;

    @Column(nullable = false)
    private String buyer;

    @Column(nullable = false)
    private String receiverName;

    @Column(nullable = false)
    private String receiverAddress;

    @Column(nullable = false)
    private String receiverDetailedAddress;

    private String receiverPhone;

    private String receiverEmail;

    private String receiverRequest; // 수령인의 요청사항 ex) 벨 누르지말고 그냥 두고 가라던지 등

    private LocalDateTime shippedDate; // 주문이 배송이 시작된 날짜. 만약 아직 배송되지 않았다면 null

    private LocalDateTime cancelledDate; // 주문이 취소된 날짜. 만약 아직 취소되지 않았다면 null

    @Column(nullable = false)
    private String tid;

    @Column(nullable = false)
    private String kakaoPayOrderId;

    /***
     * 주문 취소한 날짜를 바꾸는 함수
     */
    public void cancelOrder(){
        this.cancelledDate = LocalDateTime.now();
    }

    /***
     * 주문이 배송된 날짜를 바꾸는 함수
     */
    public void shipOrder(){
        this.shippedDate = LocalDateTime.now();
    }
}
