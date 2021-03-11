package io.seata.sample.entity;


import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单
 *
 * @author
 */
@Data
public class Order {

    private Long id;
    /**
     * 订单号
     */
    private String orderNo;

    private Long userId;
    /**
     * 产品id
     */
    private Long productId;
    /**
     * 数量
     */
    private Integer count;
    /**
     * 金额
     */
    private BigDecimal money;

    /**
     * 订单状态：0：创建中；1：已完结
     */
    private Integer status;

    public Order(String orderNo, Long userId, Long productId, Integer count, BigDecimal money, Integer status) {
        this.count = count;
        this.money = money;
        this.userId = userId;
        this.status = status;
        this.orderNo = orderNo;
        this.productId = productId;
    }
}
