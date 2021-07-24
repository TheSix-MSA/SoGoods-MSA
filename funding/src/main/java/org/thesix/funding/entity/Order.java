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

    @Column(nullable = false)
    private String receiverPhone;

    private String receiverRequest; // 수령인의 요청사항 ex) 벨 누르지말고 그냥 두고 가라던지 등

    private LocalDateTime shippedDate;

    private LocalDateTime cancelledDate;

    @Column(nullable = false)
    private String tid;
}
