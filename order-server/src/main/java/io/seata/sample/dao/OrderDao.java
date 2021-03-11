package io.seata.sample.dao;

import io.seata.sample.entity.Order;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 * @author
 */
@Repository
public interface OrderDao {

    /**
     * 创建订单
     *
     * @param order
     * @return
     */
    void create(Order order);

    /**
     * 修改订单金额
     *
     * @param userId
     * @param money
     */
    void update(@Param("userId") Long userId, @Param("money") BigDecimal money, @Param("status") Integer status);

    /**
     * 新增
     *
     * @return
     */
    void save(@Param("order") Order order);

    /**
     * 根据orderNo，更新状态
     *
     * @param orderNo
     * @param status
     */
    void updateStatusByOrderNo(@Param("orderNo") String orderNo, @Param("status") Integer status);

    /**
     * 根据orderNo，删除
     *
     * @param orderNo
     */
    void deleteByOrderNo(@Param("orderNo") String orderNo);
}
